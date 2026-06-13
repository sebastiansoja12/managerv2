package com.warehouse.shipment.application.service.returning;

import com.warehouse.shipment.application.port.primary.command.ShipmentReturnCommand;
import com.warehouse.shipment.domain.enumeration.ReturnStatus;
import com.warehouse.shipment.domain.event.ShipmentEvent;
import com.warehouse.shipment.domain.model.Shipment;

import java.util.Optional;
import java.util.Set;

public interface ShipmentReturnStrategy {

    Set<ReturnStatus> supportedStatuses();

    Optional<ShipmentEvent> process(final Shipment shipment, final ShipmentReturnCommand command);
}
