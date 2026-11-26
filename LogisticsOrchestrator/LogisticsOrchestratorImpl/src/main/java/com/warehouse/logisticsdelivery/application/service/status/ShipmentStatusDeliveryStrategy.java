package com.warehouse.logisticsdelivery.application.service.status;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.logisticsdelivery.application.port.primary.command.ApplyShipmentStatusCommand;

import java.util.Set;

public interface ShipmentStatusDeliveryStrategy {

    Set<ShipmentStatus> supportedStatuses();

    void apply(final ApplyShipmentStatusCommand command);
}
