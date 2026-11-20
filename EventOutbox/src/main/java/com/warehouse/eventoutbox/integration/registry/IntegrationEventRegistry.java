package com.warehouse.eventoutbox.integration.registry;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import com.warehouse.eventoutbox.integration.annotation.IntegrationEventType;
import com.warehouse.eventoutbox.integration.model.IntegrationEvent;
import com.warehouse.eventoutbox.integration.model.IntegrationEventKey;

public class IntegrationEventRegistry {

    private final Map<Class<? extends IntegrationEvent>, IntegrationEventDefinition<?>> definitionsByClass =
            new LinkedHashMap<>();
    private final Map<String, IntegrationEventDefinition<?>> definitionsByType = new HashMap<>();

    public <E extends IntegrationEvent> void register(final Class<E> eventClass) {
        final IntegrationEventType eventType = eventClass.getDeclaredAnnotation(IntegrationEventType.class);
        if (eventType == null) {
            throw new IllegalArgumentException(
                    "Integration event does not declare @IntegrationEventType: " + eventClass.getName());
        }
        register(new IntegrationEventDefinition<>(eventClass, eventType.value(), eventType.version(),
                eventType.topic(), keyExtractor(eventClass)));
    }

    public IntegrationEventDefinition<?> definitionOf(final Class<? extends IntegrationEvent> eventClass) {
        final IntegrationEventDefinition<?> definition = this.definitionsByClass.get(eventClass);
        if (definition == null) {
            throw new IllegalStateException(
                    "Integration event is not registered, declare @IntegrationEventType on " + eventClass.getName());
        }
        return definition;
    }

    public Optional<IntegrationEventDefinition<?>> findByType(final String type, final int version) {
        return Optional.ofNullable(this.definitionsByType.get(typeKey(type, version)));
    }

    public Optional<IntegrationEventDefinition<?>> findByClassName(final String className) {
        return this.definitionsByClass.values().stream()
                .filter(definition -> definition.eventClass().getName().equals(className))
                .findFirst();
    }

    public Collection<IntegrationEventDefinition<?>> definitions() {
        return Collections.unmodifiableCollection(this.definitionsByClass.values());
    }

    private <E extends IntegrationEvent> void register(final IntegrationEventDefinition<E> definition) {
        final Class<E> eventClass = definition.eventClass();
        final String typeKey = typeKey(definition.type(), definition.version());
        if (this.definitionsByClass.containsKey(eventClass)) {
            throw new IllegalStateException("Integration event is registered more than once: " + eventClass.getName());
        }
        final IntegrationEventDefinition<?> sameType = this.definitionsByType.get(typeKey);
        if (sameType != null) {
            throw new IllegalStateException("Integration event type " + typeKey + " is declared by both "
                    + sameType.eventClass().getName() + " and " + eventClass.getName());
        }
        this.definitionsByClass.put(eventClass, definition);
        this.definitionsByType.put(typeKey, definition);
    }

    private <E extends IntegrationEvent> Function<E, String> keyExtractor(final Class<E> eventClass) {
        if (IntegrationEventKey.class.isAssignableFrom(eventClass)) {
            return event -> ((IntegrationEventKey) event).eventKey();
        }
        return event -> null;
    }

    private String typeKey(final String type, final int version) {
        return type + "@" + version;
    }
}
