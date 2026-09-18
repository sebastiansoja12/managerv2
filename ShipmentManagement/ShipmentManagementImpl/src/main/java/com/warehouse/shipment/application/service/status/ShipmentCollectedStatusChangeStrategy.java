package com.warehouse.shipment.application.service.status;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.shipment.domain.event.ShipmentCollected;
import com.warehouse.shipment.domain.event.ShipmentEvent;
import com.warehouse.shipment.domain.model.Shipment;

import java.time.Instant;
import java.util.Set;

public class ShipmentCollectedStatusChangeStrategy implements ShipmentStatusChangeStrategy {

    @Override
    public Set<ShipmentStatus> supportedStatuses() {
        return Set.of(ShipmentStatus.COLLECTED);
    }

    @Override
    public ShipmentEvent process(final Shipment shipment) {
        shipment.notifyShipmentCollected();
        return new ShipmentCollected(shipment.snapshot(), Instant.now());
    }
}
