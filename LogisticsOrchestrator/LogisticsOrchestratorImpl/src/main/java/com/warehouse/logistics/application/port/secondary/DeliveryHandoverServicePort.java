package com.warehouse.logistics.application.port.secondary;

import com.warehouse.logistics.application.port.primary.command.ShipmentHandoverCommand;
import com.warehouse.logistics.application.port.primary.result.ShipmentHandoverResult;

public interface DeliveryHandoverServicePort {
    ShipmentHandoverResult handOver(final ShipmentHandoverCommand command);
}
