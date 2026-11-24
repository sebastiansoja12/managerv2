package com.warehouse.logisticsdelivery.application.service.status;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.logisticsdelivery.application.port.primary.DeliveryPort;
import com.warehouse.logisticsdelivery.application.port.primary.command.ApplyShipmentStatusCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.CompleteDeliveryCommand;

import java.util.Set;

public class ShipmentDeliveredDeliveryStrategy implements ShipmentStatusDeliveryStrategy {

    private final DeliveryPort deliveryPort;

    public ShipmentDeliveredDeliveryStrategy(final DeliveryPort deliveryPort) {
        this.deliveryPort = deliveryPort;
    }

    @Override
    public Set<ShipmentStatus> supportedStatuses() {
        return Set.of(ShipmentStatus.DELIVERY);
    }

    @Override
    public void apply(final ApplyShipmentStatusCommand command) {
        deliveryPort.completeDelivery(new CompleteDeliveryCommand(command.shipmentId(), null, command.userId(),
                command.departmentId()));
    }
}
