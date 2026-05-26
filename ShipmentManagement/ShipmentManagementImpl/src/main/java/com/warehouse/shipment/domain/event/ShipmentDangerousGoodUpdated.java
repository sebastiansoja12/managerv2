package com.warehouse.shipment.domain.event;

import java.time.Instant;

import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

public class ShipmentDangerousGoodUpdated extends ShipmentChangedEvent implements ShipmentEvent {

    public ShipmentDangerousGoodUpdated(final ShipmentSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }
}
