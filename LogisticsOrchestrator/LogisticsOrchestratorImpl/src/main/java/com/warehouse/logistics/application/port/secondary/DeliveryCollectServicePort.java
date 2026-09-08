package com.warehouse.logistics.application.port.secondary;

import com.warehouse.logistics.application.port.primary.command.ShipmentCollectCommand;
import com.warehouse.logistics.application.port.primary.result.ShipmentCollectResult;


public interface DeliveryCollectServicePort {
    ShipmentCollectResult collect(final ShipmentCollectCommand command);
}
