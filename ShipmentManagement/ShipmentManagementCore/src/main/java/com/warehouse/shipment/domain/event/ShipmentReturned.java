package com.warehouse.shipment.domain.event;

import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

import java.time.Instant;

public class ShipmentReturned extends ShipmentStatusChangedEvent implements ShipmentEvent {
    public ShipmentReturned(final ShipmentSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }
}
