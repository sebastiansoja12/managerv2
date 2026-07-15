package com.warehouse.shipment.domain.event;

import java.time.Instant;

import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

public class ShipmentDangerousGoodRemoved extends ShipmentChanged implements ShipmentEvent {

    public ShipmentDangerousGoodRemoved(final ShipmentSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }
}
