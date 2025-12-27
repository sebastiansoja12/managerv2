package com.warehouse.process.domain.port.primary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.process.domain.vo.ProcessId;

public class ProcessPortImpl implements ProcessPort {
    @Override
    public ProcessId initialize(final ShipmentId shipmentId) {
        return null;
    }
}
