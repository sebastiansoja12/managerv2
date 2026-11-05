package com.warehouse.shipment.domain.event;

import com.warehouse.commonassets.event.domain.model.DomainEvent;
import com.warehouse.commonassets.identificator.ShipmentId;

import java.time.Instant;
import java.util.Objects;

public class ShipmentNoteAdded implements DomainEvent {

    private final ShipmentId shipmentId;

    private final boolean internal;

    private final Instant timestamp;

    public ShipmentNoteAdded(final ShipmentId shipmentId, final boolean internal, final Instant timestamp) {
        this.shipmentId = Objects.requireNonNull(shipmentId, "Shipment ID cannot be null");
        this.internal = internal;
        this.timestamp = Objects.requireNonNull(timestamp, "Timestamp cannot be null");
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public boolean isInternal() {
        return internal;
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }
}
