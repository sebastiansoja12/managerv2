package com.warehouse.shipment.application.service.status;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.shipment.domain.event.ShipmentEvent;
import com.warehouse.shipment.domain.model.Shipment;

import java.util.Optional;
import java.util.Set;

public class ShipmentUnchangedStatusChangeStrategy implements ShipmentStatusChangeStrategy {

    @Override
    public Set<ShipmentStatus> supportedStatuses() {
        return Set.of(ShipmentStatus.PREPARED, ShipmentStatus.ACCEPTED, ShipmentStatus.CANCELED);
    }

    @Override
    public Optional<ShipmentEvent> process(final Shipment shipment) {
        return Optional.empty();
    }
}
