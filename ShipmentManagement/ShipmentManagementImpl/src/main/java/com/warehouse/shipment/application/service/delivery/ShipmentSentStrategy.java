package com.warehouse.shipment.application.service.delivery;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.shipment.domain.event.ShipmentEvent;
import com.warehouse.shipment.domain.event.ShipmentSent;
import com.warehouse.shipment.domain.model.Shipment;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

public class ShipmentSentStrategy implements ShipmentDeliveryStrategy {

    private static final Set<DeliveryStatus> SUPPORTED_STATUSES =
            Set.of(DeliveryStatus.UNKNOWN, DeliveryStatus.LOST);

    @Override
    public Set<DeliveryStatus> supportedStatuses() {
        return SUPPORTED_STATUSES;
    }

    @Override
    public Optional<ShipmentEvent> process(final Shipment shipment) {
        shipment.notifyShipmentSent();
        return Optional.of(new ShipmentSent(shipment.snapshot(), Instant.now()));
    }
}
