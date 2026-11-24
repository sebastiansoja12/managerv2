package com.warehouse.logisticsdelivery.application.service.status;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.logisticsdelivery.application.port.primary.DeliveryPort;
import com.warehouse.logisticsdelivery.application.port.primary.command.ApplyShipmentStatusCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.CreateDerivedDeliveryCommand;

import java.util.Set;

public class ShipmentReturnedDeliveryStrategy implements ShipmentStatusDeliveryStrategy {

    private final DeliveryPort deliveryPort;

    public ShipmentReturnedDeliveryStrategy(final DeliveryPort deliveryPort) {
        this.deliveryPort = deliveryPort;
    }

    @Override
    public Set<ShipmentStatus> supportedStatuses() {
        return Set.of(ShipmentStatus.RETURN);
    }

    @Override
    public void apply(final ApplyShipmentStatusCommand command) {
        deliveryPort.createDerivedDelivery(new CreateDerivedDeliveryCommand(command.shipmentRelatedId(),
                command.signatureRequired(), command.userId()));
    }
}
