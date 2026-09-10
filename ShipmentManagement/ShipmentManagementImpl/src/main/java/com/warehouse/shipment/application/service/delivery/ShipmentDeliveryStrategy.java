package com.warehouse.shipment.application.service.delivery;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.shipment.domain.event.ShipmentEvent;
import com.warehouse.shipment.domain.model.Shipment;

import java.util.Optional;
import java.util.Set;

public interface ShipmentDeliveryStrategy {

    Set<DeliveryStatus> supportedStatuses();

    Optional<ShipmentEvent> process(Shipment shipment);
}
