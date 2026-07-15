package com.warehouse.shipment.application.service.status;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.shipment.domain.event.ShipmentEvent;
import com.warehouse.shipment.domain.event.ShipmentRerouted;
import com.warehouse.shipment.domain.model.Shipment;

import java.time.Instant;
import java.util.Set;

public class ShipmentReroutedStatusChangeStrategy implements ShipmentStatusChangeStrategy {

    @Override
    public Set<ShipmentStatus> supportedStatuses() {
        return Set.of(ShipmentStatus.REROUTE);
    }

    @Override
    public ShipmentEvent process(final Shipment shipment) {
        shipment.notifyShipmentRerouted();
        return new ShipmentRerouted(shipment.snapshot(), Instant.now());
    }
}
