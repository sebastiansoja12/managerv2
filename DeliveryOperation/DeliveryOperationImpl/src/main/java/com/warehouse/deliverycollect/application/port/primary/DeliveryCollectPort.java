package com.warehouse.deliverycollect.application.port.primary;

import com.warehouse.deliverycollect.application.port.primary.command.DeliveryCollectCommand;
import com.warehouse.deliverycollect.application.port.primary.result.DeliveryCollectResult;

public interface DeliveryCollectPort {
    DeliveryCollectResult collect(final DeliveryCollectCommand command);
}
