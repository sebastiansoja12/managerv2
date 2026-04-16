package com.warehouse.shipment.domain.event;

import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

import java.time.Instant;

public class ShipmentStatusChangedEvent extends ShipmentChangedEvent implements ShipmentEvent {
    public ShipmentStatusChangedEvent(final ShipmentSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }
}
