package com.warehouse.shipment.application.service.status;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.shipment.domain.event.ShipmentEvent;
import com.warehouse.shipment.domain.model.Shipment;

import java.util.Optional;
import java.util.Set;

public class ShipmentCreatedStatusChangeStrategy implements ShipmentStatusChangeStrategy {

    @Override
    public Set<ShipmentStatus> supportedStatuses() {
        return Set.of(ShipmentStatus.CREATED);
    }

    @Override
    public Optional<ShipmentEvent> process(final Shipment shipment) {
        throw new IllegalStateException("Shipment already created, status cannot be changed");
    }
}
