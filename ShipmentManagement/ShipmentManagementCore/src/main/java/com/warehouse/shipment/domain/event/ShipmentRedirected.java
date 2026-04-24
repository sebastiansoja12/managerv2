package com.warehouse.shipment.domain.event;

import java.time.Instant;

import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

public class ShipmentRedirected extends ShipmentStatusChangedEvent implements ShipmentEvent {
    public ShipmentRedirected(final ShipmentSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }
}
