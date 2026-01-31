package com.warehouse.shipment.domain.event;

import java.time.Instant;

import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

public class ShipmentRelatedLocked extends ShipmentStatusChangedEvent implements ShipmentEvent {
    public ShipmentRelatedLocked(final ShipmentSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }
}
