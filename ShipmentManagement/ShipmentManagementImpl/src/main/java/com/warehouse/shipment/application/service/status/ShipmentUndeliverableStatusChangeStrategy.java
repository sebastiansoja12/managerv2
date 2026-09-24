package com.warehouse.shipment.application.service.status;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.shipment.domain.event.ShipmentEvent;
import com.warehouse.shipment.domain.event.ShipmentStatusChanged;
import com.warehouse.shipment.domain.model.Shipment;

import java.time.Instant;
import java.util.Set;

public class ShipmentUndeliverableStatusChangeStrategy implements ShipmentStatusChangeStrategy {

    @Override
    public Set<ShipmentStatus> supportedStatuses() {
        return Set.of(ShipmentStatus.UNDELIVERABLE);
    }

    @Override
    public ShipmentEvent process(final Shipment shipment) {
        shipment.markAsUndeliverable();
        return new ShipmentStatusChanged(shipment.snapshot(), Instant.now());
    }
}
