package com.warehouse.shipment.domain.event;

import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public class ShipmentChangedEvent {

    @NotNull
    private final ShipmentSnapshot snapshot;

    @NotNull
    private Instant timestamp;

    public ShipmentChangedEvent(final ShipmentSnapshot snapshot, final Instant timestamp) {
        this.snapshot = snapshot;
    }

    public ShipmentSnapshot getSnapshot() {
        return snapshot;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
