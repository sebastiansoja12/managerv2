package com.warehouse.shipment.domain.event;

import java.time.Instant;

import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

public class ShipmentReturnCanceled extends ShipmentStatusChangedEvent implements ShipmentEvent {
    public ShipmentReturnCanceled(final ShipmentSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }
}
