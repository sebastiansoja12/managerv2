package com.warehouse.shipment.domain.event;

import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

import java.time.Instant;

public class ShipmentCanceled extends ShipmentStatusChanged implements ShipmentEvent {
    public ShipmentCanceled(final ShipmentSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }

    public ShipmentCanceled(final ShipmentSnapshot previousSnapshot,
                            final ShipmentSnapshot snapshot,
                            final Instant timestamp) {
        super(previousSnapshot, snapshot, timestamp);
    }
}
