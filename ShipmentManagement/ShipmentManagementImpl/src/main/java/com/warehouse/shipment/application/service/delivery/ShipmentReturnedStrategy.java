package com.warehouse.shipment.application.service.delivery;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.shipment.domain.event.ShipmentEvent;
import com.warehouse.shipment.domain.event.ShipmentReturned;
import com.warehouse.shipment.domain.model.Shipment;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

public class ShipmentReturnedStrategy implements ShipmentDeliveryStrategy {

    @Override
    public Set<DeliveryStatus> supportedStatuses() {
        return Set.of(DeliveryStatus.RETURN);
    }

    @Override
    public Optional<ShipmentEvent> process(final Shipment shipment) {
        shipment.notifyShipmentReturned();
        return Optional.of(new ShipmentReturned(shipment.snapshot(), Instant.now()));
    }
}
