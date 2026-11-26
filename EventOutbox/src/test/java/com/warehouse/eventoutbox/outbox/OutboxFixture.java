package com.warehouse.eventoutbox.outbox;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

import com.warehouse.eventoutbox.outbox.domain.enumeration.OutboxEventStatus;
import com.warehouse.eventoutbox.outbox.domain.model.OutboxEvent;
import com.warehouse.eventoutbox.outbox.domain.service.OutboxRetryPolicy;
import com.warehouse.eventoutbox.integration.model.EventId;

public final class OutboxFixture {

    public static final Instant NOW = Instant.parse("2026-11-18T10:00:00Z");

    private OutboxFixture() {
    }

    public static OutboxEvent pendingEvent(final String messageKey) {
        return pendingEvent(messageKey, NOW);
    }

    public static OutboxEvent pendingEvent(final String messageKey, final Instant occurredAt) {
        return new OutboxEvent(
                EventId.nextId(),
                "shipment.created",
                1,
                "shipment.created",
                messageKey,
                "{\"shipmentId\":1}",
                Map.of("ce_type", "shipment.created"),
                occurredAt,
                OutboxEventStatus.PENDING,
                0,
                occurredAt,
                null,
                null);
    }

    public static OutboxRetryPolicy retryPolicy() {
        return new OutboxRetryPolicy(Duration.ofSeconds(5), Duration.ofMinutes(5), 10);
    }
}
