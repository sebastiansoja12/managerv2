package com.warehouse.shipment.application.service.delivery;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.shipment.domain.event.ShipmentDelivered;
import com.warehouse.shipment.domain.event.ShipmentEvent;
import com.warehouse.shipment.domain.model.Shipment;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

public class ShipmentDeliveredStrategy implements ShipmentDeliveryStrategy {

    private static final Set<DeliveryStatus> SUPPORTED_STATUSES =
            Set.of(DeliveryStatus.DELIVERY, DeliveryStatus.DEPOT, DeliveryStatus.DELIVERED);

    @Override
    public Set<DeliveryStatus> supportedStatuses() {
        return SUPPORTED_STATUSES;
    }

    @Override
    public Optional<ShipmentEvent> process(final Shipment shipment) {
        shipment.notifyShipmentDelivered();
        return Optional.of(new ShipmentDelivered(shipment.snapshot(), Instant.now()));
    }
}
