package com.warehouse.shipment.application.service.status;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.shipment.domain.event.ShipmentEvent;
import com.warehouse.shipment.domain.event.ShipmentReturned;
import com.warehouse.shipment.domain.model.Shipment;

import java.time.Instant;
import java.util.Set;

public class ShipmentReturnedStatusChangeStrategy implements ShipmentStatusChangeStrategy {

    @Override
    public Set<ShipmentStatus> supportedStatuses() {
        return Set.of(ShipmentStatus.RETURN);
    }

    @Override
    public ShipmentEvent process(final Shipment shipment) {
        shipment.notifyShipmentReturned();
        return new ShipmentReturned(shipment.snapshot(), Instant.now());
    }
}
