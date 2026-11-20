package com.warehouse.eventoutbox.outbox.domain.service;

import java.util.Map;

import com.warehouse.eventoutbox.integration.model.EventMetadata;
import com.warehouse.eventoutbox.outbox.domain.enumeration.OutboxEventStatus;
import com.warehouse.eventoutbox.outbox.domain.model.OutboxEvent;

public class OutboxEventFactory {

    public OutboxEvent create(final EventMetadata metadata,
                              final String topic,
                              final String payload,
                              final Map<String, String> headers) {
        return new OutboxEvent(
                metadata.eventId(),
                metadata.eventType(),
                metadata.eventVersion(),
                topic,
                metadata.subject(),
                payload,
                headers,
                metadata.occurredAt(),
                OutboxEventStatus.PENDING,
                0,
                metadata.occurredAt(),
                null,
                null);
    }
}
