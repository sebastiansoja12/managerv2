package com.warehouse.logisticsdelivery.application.service.status;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.logisticsdelivery.application.port.primary.DeliveryPort;
import com.warehouse.logisticsdelivery.application.port.primary.command.ApplyShipmentStatusCommand;

import java.util.Set;

public class ShipmentLostDeliveryStrategy implements ShipmentStatusDeliveryStrategy {

    private final DeliveryPort deliveryPort;

    public ShipmentLostDeliveryStrategy(final DeliveryPort deliveryPort) {
        this.deliveryPort = deliveryPort;
    }

    @Override
    public Set<ShipmentStatus> supportedStatuses() {
        return Set.of(ShipmentStatus.LOST);
    }

    @Override
    public void apply(final ApplyShipmentStatusCommand command) {
        deliveryPort.cancelShipmentDeliveries(command.shipmentId());
    }
}
