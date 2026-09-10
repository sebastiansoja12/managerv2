package com.warehouse.shipment.application.service.delivery;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.event.ShipmentEvent;
import com.warehouse.shipment.domain.event.ShipmentRedirected;
import com.warehouse.shipment.domain.model.Shipment;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

public class ShipmentRedirectedStrategy implements ShipmentDeliveryStrategy {

    private static final Set<DeliveryStatus> SUPPORTED_STATUSES =
            Set.of(DeliveryStatus.UNAVAILABLE, DeliveryStatus.REJECTED, DeliveryStatus.SENDER);

    @Override
    public Set<DeliveryStatus> supportedStatuses() {
        return SUPPORTED_STATUSES;
    }

    @Override
    public Optional<ShipmentEvent> process(final Shipment shipment) {
        shipment.notifyRelatedShipmentRedirected(ShipmentId.nextId());
        return Optional.of(new ShipmentRedirected(shipment.snapshot(), Instant.now()));
    }
}
