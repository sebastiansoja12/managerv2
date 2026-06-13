package com.warehouse.shipment.application.event;

import java.time.Instant;
import java.util.UUID;

import com.warehouse.commonassets.kafka.domain.model.OperatorAwareContext;
import com.warehouse.shipment.application.event.snapshot.ShipmentSnapshot;

public final class ShipmentCreatedIntegrationEvent extends OperatorAwareContext {

    private final UUID eventId;
    private final String eventType;
    private final int version;
    private final Instant occurredAt;
    private final ShipmentSnapshot payload;

    public ShipmentCreatedIntegrationEvent(final UUID eventId,
                                           final String eventType,
                                           final int version,
                                           final Instant occurredAt,
                                           final ShipmentSnapshot payload) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.version = version;
        this.occurredAt = occurredAt;
        this.payload = payload;
    }

    public UUID eventId() {
        return eventId;
    }

    public String eventType() {
        return eventType;
    }

    public int version() {
        return version;
    }

    public Instant occurredAt() {
        return occurredAt;
    }

    public ShipmentSnapshot payload() {
        return payload;
    }

    public UUID getEventId() {
        return eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public int getVersion() {
        return version;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public ShipmentSnapshot getPayload() {
        return payload;
    }
}
