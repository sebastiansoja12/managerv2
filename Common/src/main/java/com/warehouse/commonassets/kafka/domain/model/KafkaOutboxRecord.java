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
                                Map<String, String> headers) {
}
