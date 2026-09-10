package com.warehouse.shipment.domain.event;

import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

import java.time.Instant;

public class ShipmentDangerousGoodAdded extends ShipmentChanged implements ShipmentEvent {

    public ShipmentDangerousGoodAdded(final ShipmentSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }
}
