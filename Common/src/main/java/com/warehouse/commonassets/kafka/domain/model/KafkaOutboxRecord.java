package com.warehouse.commonassets.kafka.domain.model;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import com.warehouse.commonassets.identificator.OperatorId;

public record KafkaOutboxRecord(UUID eventId,
                                String topic,
                                String messageKey,
                                String eventType,
                                int eventVersion,
                                Instant occurredAt,
                                OperatorId operatorId,
                                String payload,
                                Map<String, String> headers,
                                KafkaOutboxStatus status,
                                int attemptCount) {

    public KafkaOutboxRecord(final UUID eventId,
                             final String topic,
                             final String messageKey,
                             final String eventType,
                             final int eventVersion,
                             final Instant occurredAt,
                             final OperatorId operatorId,
                             final String payload,
                             final Map<String, String> headers) {
        this(eventId, topic, messageKey, eventType, eventVersion, occurredAt, operatorId, payload, headers,
                KafkaOutboxStatus.PENDING, 0);
    }
}
