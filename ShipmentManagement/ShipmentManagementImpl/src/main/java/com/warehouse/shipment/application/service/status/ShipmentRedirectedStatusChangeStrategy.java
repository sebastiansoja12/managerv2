package com.warehouse.shipment.application.service.status;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.event.ShipmentEvent;
import com.warehouse.shipment.domain.event.ShipmentRedirected;
import com.warehouse.shipment.domain.model.Shipment;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

public class ShipmentRedirectedStatusChangeStrategy implements ShipmentStatusChangeStrategy {

    @Override
    public Set<ShipmentStatus> supportedStatuses() {
        return Set.of(ShipmentStatus.REDIRECT);
    }

    @Override
    public Optional<ShipmentEvent> process(final Shipment shipment) {
        shipment.notifyRelatedShipmentRedirected(ShipmentId.nextId());
        return Optional.of(new ShipmentRedirected(shipment.snapshot(), Instant.now()));
    }
}
