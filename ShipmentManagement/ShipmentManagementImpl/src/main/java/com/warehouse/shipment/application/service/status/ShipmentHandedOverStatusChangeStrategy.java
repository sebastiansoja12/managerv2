package com.warehouse.shipment.application.service.status;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.shipment.domain.event.ShipmentEvent;
import com.warehouse.shipment.domain.event.ShipmentStatusChanged;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

import java.time.Instant;
import java.util.Set;

public class ShipmentHandedOverStatusChangeStrategy implements ShipmentStatusChangeStrategy {

    @Override
    public Set<ShipmentStatus> supportedStatuses() {
        return Set.of(ShipmentStatus.HANDOVER);
    }

    @Override
    public ShipmentEvent process(final Shipment shipment) {
        final ShipmentSnapshot previousSnapshot = shipment.snapshot();
        shipment.handOver();
        return new ShipmentStatusChanged(previousSnapshot, shipment.snapshot(), Instant.now());
    }
}
