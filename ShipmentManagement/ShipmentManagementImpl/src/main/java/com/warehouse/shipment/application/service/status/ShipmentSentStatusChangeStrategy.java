package com.warehouse.shipment.application.service.status;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.shipment.domain.event.ShipmentEvent;
import com.warehouse.shipment.domain.event.ShipmentSent;
import com.warehouse.shipment.domain.model.Shipment;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

public class ShipmentSentStatusChangeStrategy implements ShipmentStatusChangeStrategy {

    @Override
    public Set<ShipmentStatus> supportedStatuses() {
        return Set.of(ShipmentStatus.SENT);
    }

    @Override
    public Optional<ShipmentEvent> process(final Shipment shipment) {
        shipment.notifyShipmentSent();
        return Optional.of(new ShipmentSent(shipment.snapshot(), Instant.now()));
    }
}
