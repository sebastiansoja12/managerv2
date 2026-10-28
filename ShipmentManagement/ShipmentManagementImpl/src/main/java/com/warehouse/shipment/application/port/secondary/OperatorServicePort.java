package com.warehouse.shipment.application.port.secondary;

import com.warehouse.commonassets.identificator.OperatorId;

public interface OperatorServicePort {
    boolean isActive(final OperatorId operatorId);
}
