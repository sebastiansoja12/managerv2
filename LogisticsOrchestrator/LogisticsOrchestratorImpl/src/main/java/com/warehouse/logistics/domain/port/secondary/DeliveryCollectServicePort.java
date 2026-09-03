package com.warehouse.logistics.domain.port.secondary;

import com.warehouse.logistics.domain.vo.ShipmentCollectCommand;
import com.warehouse.logistics.domain.vo.ShipmentCollectResult;

public interface DeliveryCollectServicePort {
    ShipmentCollectResult collect(final ShipmentCollectCommand command);
}
