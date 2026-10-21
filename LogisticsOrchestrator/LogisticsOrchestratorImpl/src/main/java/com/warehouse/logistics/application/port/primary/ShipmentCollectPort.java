package com.warehouse.logistics.application.port.primary;

import com.warehouse.logistics.application.port.primary.command.ShipmentCollectCommand;
import com.warehouse.logistics.application.port.primary.result.ShipmentCollectResult;


public interface ShipmentCollectPort {
    ShipmentCollectResult collect(final ShipmentCollectCommand command);
}
