package com.warehouse.commonassets.kafka.domain.model;

import java.util.Objects;

public record KafkaEventMessage<T>(String eventType, T event) {

    public static <T> KafkaEventMessage<T> from(final T event) {
        final T requiredEvent = Objects.requireNonNull(event, "Kafka event cannot be null");
        return new KafkaEventMessage<>(requiredEvent.getClass().getSimpleName(), requiredEvent);
    }
}
