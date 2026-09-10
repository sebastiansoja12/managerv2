package com.warehouse.logistics.application.port.primary;

import com.warehouse.logistics.application.port.primary.command.ShipmentCollectCommand;
import com.warehouse.logistics.application.port.primary.result.ShipmentCollectResult;
import com.warehouse.logistics.application.port.secondary.DeliveryCollectServicePort;


public class ShipmentCollectPortImpl implements ShipmentCollectPort {

    private final DeliveryCollectServicePort deliveryCollectServicePort;

    public ShipmentCollectPortImpl(final DeliveryCollectServicePort deliveryCollectServicePort) {
        this.deliveryCollectServicePort = deliveryCollectServicePort;
    }

    @Override
    public ShipmentCollectResult collect(final ShipmentCollectCommand command) {
        return deliveryCollectServicePort.collect(command);
    }
}
