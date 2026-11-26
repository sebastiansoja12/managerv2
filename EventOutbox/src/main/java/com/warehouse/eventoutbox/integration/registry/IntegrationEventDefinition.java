package com.warehouse.eventoutbox.integration.registry;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import com.warehouse.eventoutbox.integration.model.IntegrationEvent;

public record IntegrationEventDefinition<E extends IntegrationEvent>(Class<E> eventClass,
                                                                     String type,
                                                                     int version,
                                                                     String topic,
                                                                     Function<E, String> keyExtractor) {

    public IntegrationEventDefinition {
        Objects.requireNonNull(eventClass, "Integration event class cannot be null");
        Objects.requireNonNull(keyExtractor, "Integration event key extractor cannot be null: " + eventClass.getName());
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Integration event type cannot be blank: " + eventClass.getName());
        }
        if (version <= 0) {
            throw new IllegalArgumentException("Integration event version must be positive: " + eventClass.getName());
        }
        if (topic == null || topic.isBlank()) {
            throw new IllegalArgumentException("Integration event topic cannot be blank: " + eventClass.getName());
        }
    }

    public Optional<String> messageKey(final IntegrationEvent event) {
        return Optional.ofNullable(this.keyExtractor.apply(this.eventClass.cast(event)));
    }
}
