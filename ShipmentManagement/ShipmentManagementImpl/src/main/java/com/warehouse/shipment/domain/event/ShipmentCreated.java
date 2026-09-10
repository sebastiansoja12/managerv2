package com.warehouse.shipment.domain.event;

import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

import java.time.Instant;

public class ShipmentCreated extends ShipmentChanged implements ShipmentEvent {

    private final ShipmentSnapshot snapshot;

    public ShipmentCreated(final ShipmentSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
        this.snapshot = snapshot;
    }

    @Override
    public ShipmentSnapshot getSnapshot() {
        return snapshot;
    }
}
