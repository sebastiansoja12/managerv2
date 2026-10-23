package com.warehouse.logistics.application.port.primary;

import com.warehouse.logistics.application.port.primary.command.ShipmentHandoverCommand;
import com.warehouse.logistics.application.port.primary.result.ShipmentHandoverResult;

public interface ShipmentHandoverPort {
    ShipmentHandoverResult handOver(final ShipmentHandoverCommand command);
}
