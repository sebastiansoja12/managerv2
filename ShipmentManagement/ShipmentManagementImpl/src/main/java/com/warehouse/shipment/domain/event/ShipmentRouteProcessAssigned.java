package com.warehouse.shipment.domain.event;

import java.time.Instant;

import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

public class ShipmentRouteProcessAssigned extends ShipmentChangedEvent implements ShipmentEvent {
    public ShipmentRouteProcessAssigned(final ShipmentSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }
}
