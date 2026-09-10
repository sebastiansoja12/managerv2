package com.warehouse.shipment.domain.event;

import java.time.Instant;

import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

public class ShipmentLocked extends ShipmentStatusChanged implements ShipmentEvent {
    public ShipmentLocked(final ShipmentSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }
}
