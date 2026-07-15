package com.warehouse.shipment.application.service.status;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.shipment.domain.event.ShipmentDelivered;
import com.warehouse.shipment.domain.event.ShipmentEvent;
import com.warehouse.shipment.domain.model.Shipment;

import java.time.Instant;
import java.util.Set;

public class ShipmentDeliveredStatusChangeStrategy implements ShipmentStatusChangeStrategy {

    @Override
    public Set<ShipmentStatus> supportedStatuses() {
        return Set.of(ShipmentStatus.DELIVERY);
    }

    @Override
    public ShipmentEvent process(final Shipment shipment) {
        shipment.notifyShipmentDelivered();
        return new ShipmentDelivered(shipment.snapshot(), Instant.now());
    }
}
