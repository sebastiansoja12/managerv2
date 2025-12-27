package com.warehouse.process.domain.port.primary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.process.domain.vo.ProcessId;

public interface ProcessPort {
    ProcessId initialize(final ShipmentId shipmentId);
}
