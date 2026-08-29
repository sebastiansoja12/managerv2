package com.warehouse.shipment.application.service.delivery;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.shipment.domain.event.ShipmentEvent;
import com.warehouse.shipment.domain.model.Shipment;

import java.util.Optional;
import java.util.Set;

public class ShipmentUnchangedStrategy implements ShipmentDeliveryStrategy {

    @Override
    public Set<DeliveryStatus> supportedStatuses() {
        return Set.of(DeliveryStatus.CLIENT);
    }

    @Override
    public Optional<ShipmentEvent> process(final Shipment shipment) {
        return Optional.empty();
    }
}
