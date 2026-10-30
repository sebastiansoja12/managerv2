package com.warehouse.shipment.domain.event;

import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

import java.time.Instant;

public class ShipmentStatusChanged extends ShipmentChanged implements ShipmentEvent {
    public ShipmentStatusChanged(final ShipmentSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }

    public ShipmentStatusChanged(final ShipmentSnapshot previousSnapshot,
                                 final ShipmentSnapshot snapshot,
                                 final Instant timestamp) {
        super(previousSnapshot, snapshot, timestamp);
    }
}
