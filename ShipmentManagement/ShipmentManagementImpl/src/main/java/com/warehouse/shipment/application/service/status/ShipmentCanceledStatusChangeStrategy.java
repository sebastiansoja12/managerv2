package com.warehouse.shipment.application.service.status;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.shipment.domain.event.ShipmentEvent;
import com.warehouse.shipment.domain.model.Shipment;

import java.util.Set;

public class ShipmentCanceledStatusChangeStrategy implements ShipmentStatusChangeStrategy {

    @Override
    public Set<ShipmentStatus> supportedStatuses() {
        return Set.of(ShipmentStatus.CANCELED);
    }

    @Override
    public ShipmentEvent process(final Shipment shipment) {
        throw new IllegalStateException("Use the shipment cancellation operation to set CANCELED status");
    }
}
