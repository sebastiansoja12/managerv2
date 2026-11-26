package com.warehouse;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.warehouse.eventoutbox.domain.model.DomainEvent;
import com.warehouse.eventoutbox.integration.annotation.IntegrationEventTranslation;
import com.warehouse.eventoutbox.integration.annotation.IntegrationEventType;
import com.warehouse.eventoutbox.integration.model.IntegrationEvent;
import com.warehouse.eventoutbox.integration.registry.IntegrationEventRegistry;
import com.warehouse.eventoutbox.integration.registry.IntegrationEventTypeScanner;

class IntegrationEventRegistryArchitectureTest {

    private static final String BASE_PACKAGE = "com.warehouse";

    private static final JavaClasses PRODUCTION_CLASSES = new ClassFileImporter()
            .withImportOption(new ImportOption.DoNotIncludeTests())
            .importPackages(BASE_PACKAGE);

    @Test
    void shouldDeclareIntegrationEventTypeOnEveryPublishedIntegrationEvent() {
        final List<String> undeclared = publishedEventClasses().stream()
                .filter(javaClass -> !javaClass.isAnnotatedWith(IntegrationEventType.class))
                .map(JavaClass::getName)
                .sorted()
                .toList();

        assertEquals(List.of(), undeclared,
                "Every published IntegrationEvent must declare @IntegrationEventType with its topic");
    }

    @Test
    void shouldRegisterEveryDeclaredIntegrationEvent() {
        final Set<String> declared = PRODUCTION_CLASSES.stream()
                .filter(javaClass -> javaClass.isAnnotatedWith(IntegrationEventType.class))
                .map(JavaClass::getName)
                .collect(Collectors.toSet());

        final Set<String> registered = registry().definitions().stream()
                .map(definition -> definition.eventClass().getName())
                .collect(Collectors.toSet());

        assertEquals(declared, registered);
    }

    @Test
    void shouldConsumeIntegrationEventsOnlyThroughIntegrationEventListener() {
        final List<String> rawKafkaListeners = PRODUCTION_CLASSES.stream()
                .flatMap(javaClass -> javaClass.getMethods().stream())
                .filter(method -> method.isAnnotatedWith(KafkaListener.class)
                        || method.isMetaAnnotatedWith(KafkaListener.class)
                        || method.getOwner().isAnnotatedWith(KafkaListener.class))
                .map(method -> method.getOwner().getName() + "#" + method.getName())
                .distinct()
                .sorted()
                .toList();

        assertEquals(List.of(), rawKafkaListeners,
                "Consume integration events with @IntegrationEventListener instead of @KafkaListener");
    }

    @Test
    void shouldPublishTranslationsRegardlessOfFeatureFlags() {
        final List<String> conditionalTranslators = PRODUCTION_CLASSES.stream()
                .filter(javaClass -> javaClass.getMethods().stream()
                        .anyMatch(method -> method.isAnnotatedWith(IntegrationEventTranslation.class)))
                .filter(javaClass -> javaClass.isAnnotatedWith(ConditionalOnProperty.class))
                .map(JavaClass::getName)
                .sorted()
                .toList();

        assertEquals(List.of(), conditionalTranslators,
                "Classes with @IntegrationEventTranslation must not be switched off by @ConditionalOnProperty");
    }

    @Test
    void shouldTranslateOnlyDomainEvents() {
        final List<String> invalidTranslations = PRODUCTION_CLASSES.stream()
                .flatMap(javaClass -> javaClass.getMethods().stream())
                .filter(method -> method.isAnnotatedWith(IntegrationEventTranslation.class))
                .filter(method -> method.getRawParameterTypes().size() != 1
                        || !method.getRawParameterTypes().getFirst().isAssignableTo(DomainEvent.class))
                .map(method -> method.getOwner().getName() + "#" + method.getName())
                .sorted()
                .toList();

        assertEquals(List.of(), invalidTranslations,
                "@IntegrationEventTranslation methods must take exactly one DomainEvent parameter");
    }

    private List<JavaClass> publishedEventClasses() {
        return PRODUCTION_CLASSES.stream()
                .filter(javaClass -> javaClass.isAssignableTo(IntegrationEvent.class))
                .filter(javaClass -> !javaClass.isInterface())
                .filter(this::isConstructedInProductionCode)
                .toList();
    }

    private boolean isConstructedInProductionCode(final JavaClass eventClass) {
        return eventClass.getConstructorCallsToSelf().stream()
                .anyMatch(call -> !call.getOriginOwner().isAssignableTo(eventClass.getName()));
    }

    private IntegrationEventRegistry registry() {
        return new IntegrationEventTypeScanner(getClass().getClassLoader()).scan(BASE_PACKAGE);
    }
}
