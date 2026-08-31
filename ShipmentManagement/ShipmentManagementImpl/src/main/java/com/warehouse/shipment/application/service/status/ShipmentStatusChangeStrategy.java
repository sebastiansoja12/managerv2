package com.warehouse.shipment.application.service.status;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.shipment.domain.event.ShipmentEvent;
import com.warehouse.shipment.domain.model.Shipment;

import java.util.Optional;
import java.util.Set;

public interface ShipmentStatusChangeStrategy {

    Set<ShipmentStatus> supportedStatuses();

    Optional<ShipmentEvent> process(final Shipment shipment);
}
