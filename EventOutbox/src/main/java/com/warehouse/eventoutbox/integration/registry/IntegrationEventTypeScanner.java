package com.warehouse.eventoutbox.integration.registry;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

import com.warehouse.eventoutbox.integration.annotation.IntegrationEventType;
import com.warehouse.eventoutbox.integration.model.IntegrationEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IntegrationEventTypeScanner {

    private final ClassLoader classLoader;

    public IntegrationEventTypeScanner(final ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public IntegrationEventRegistry scan(final String basePackage) {
        final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false) {
            @Override
            protected boolean isCandidateComponent(final AnnotatedBeanDefinition beanDefinition) {
                return beanDefinition.getMetadata().isIndependent() && !beanDefinition.getMetadata().isInterface();
            }
        };
        provider.setResourceLoader(new DefaultResourceLoader(this.classLoader));
        provider.addIncludeFilter(new AnnotationTypeFilter(IntegrationEventType.class, false));

        final IntegrationEventRegistry registry = new IntegrationEventRegistry();
        provider.findCandidateComponents(basePackage).stream()
                .map(BeanDefinition::getBeanClassName)
                .sorted()
                .map(className -> ClassUtils.resolveClassName(className, this.classLoader))
                .forEach(eventClass -> registry.register(integrationEventClass(eventClass)));
        registry.definitions().forEach(definition -> log.info("[events] {}@{} -> {} {}",
                definition.type(), definition.version(), definition.topic(), definition.eventClass().getName()));
        return registry;
    }

    private Class<? extends IntegrationEvent> integrationEventClass(final Class<?> eventClass) {
        if (!IntegrationEvent.class.isAssignableFrom(eventClass)) {
            throw new IllegalStateException(
                    "@IntegrationEventType is declared on a class that is not an IntegrationEvent: "
                            + eventClass.getName());
        }
        return eventClass.asSubclass(IntegrationEvent.class);
    }
}
