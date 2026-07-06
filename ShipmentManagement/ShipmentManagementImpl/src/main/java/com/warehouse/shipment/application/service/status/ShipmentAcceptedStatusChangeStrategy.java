package com.warehouse.shipment.application.service.status;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.shipment.domain.event.ShipmentEvent;
import com.warehouse.shipment.domain.event.ShipmentStatusChanged;
import com.warehouse.shipment.domain.model.Shipment;

import java.time.Instant;
import java.util.Set;

public class ShipmentAcceptedStatusChangeStrategy implements ShipmentStatusChangeStrategy {

    @Override
    public Set<ShipmentStatus> supportedStatuses() {
        return Set.of(ShipmentStatus.ACCEPTED);
    }

    @Override
    public ShipmentEvent process(final Shipment shipment) {
        shipment.notifyShipmentAccepted();
        return new ShipmentStatusChanged(shipment.snapshot(), Instant.now());
    }
}
