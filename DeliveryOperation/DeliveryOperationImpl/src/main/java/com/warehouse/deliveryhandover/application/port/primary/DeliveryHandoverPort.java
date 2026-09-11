package com.warehouse.deliveryhandover.application.port.primary;

import com.warehouse.deliveryhandover.application.port.primary.command.DeliveryHandoverCommand;
import com.warehouse.deliveryhandover.application.port.primary.result.DeliveryHandoverResult;

public interface DeliveryHandoverPort {
    DeliveryHandoverResult handOver(final DeliveryHandoverCommand command);
}
