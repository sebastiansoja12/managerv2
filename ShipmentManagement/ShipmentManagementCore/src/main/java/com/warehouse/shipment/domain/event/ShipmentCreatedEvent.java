package com.warehouse.shipment.domain.event;

import java.time.Instant;

import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

public class ShipmentCreatedEvent extends ShipmentChangedEvent implements ShipmentEvent {

    private final ShipmentSnapshot snapshot;

    public ShipmentCreatedEvent(final ShipmentSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
        this.snapshot = snapshot;
    }

    @Override
    public ShipmentSnapshot getSnapshot() {
        return snapshot;
    }
}
