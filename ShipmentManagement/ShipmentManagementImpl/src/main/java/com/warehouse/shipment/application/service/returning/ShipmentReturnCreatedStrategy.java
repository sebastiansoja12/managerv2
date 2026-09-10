package com.warehouse.shipment.application.service.returning;

import com.warehouse.shipment.application.port.primary.command.ShipmentReturnCommand;
import com.warehouse.shipment.domain.enumeration.ReturnStatus;
import com.warehouse.shipment.domain.event.ShipmentEvent;
import com.warehouse.shipment.domain.event.ShipmentReturnCreated;
import com.warehouse.shipment.domain.model.Shipment;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

public class ShipmentReturnCreatedStrategy implements ShipmentReturnStrategy {

    @Override
    public Set<ReturnStatus> supportedStatuses() {
        return Set.of(ReturnStatus.CREATED);
    }

    @Override
    public Optional<ShipmentEvent> process(final Shipment shipment, final ShipmentReturnCommand command) {
        shipment.notifyShipmentReturned();
        return Optional.of(new ShipmentReturnCreated(
                shipment.snapshot(),
                command.getReasonCode(),
                command.getReason(),
                command.getDepartmentCode(),
                Instant.now()
        ));
    }
}
