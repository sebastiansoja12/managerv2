package com.warehouse.logistics.domain.port.primary;

import com.warehouse.logistics.domain.vo.ShipmentCollectCommand;
import com.warehouse.logistics.domain.vo.ShipmentCollectResult;

public interface ShipmentCollectPort {
    ShipmentCollectResult collect(final ShipmentCollectCommand command);
}
