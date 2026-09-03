package com.warehouse.logistics.domain.port.primary;

import com.warehouse.logistics.domain.port.secondary.DeliveryCollectServicePort;
import com.warehouse.logistics.domain.vo.ShipmentCollectCommand;
import com.warehouse.logistics.domain.vo.ShipmentCollectResult;

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
