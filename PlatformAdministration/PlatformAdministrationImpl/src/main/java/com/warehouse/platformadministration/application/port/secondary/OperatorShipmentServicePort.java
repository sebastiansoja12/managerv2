package com.warehouse.platformadministration.application.port.secondary;

import com.warehouse.platformadministration.domain.vo.OperatorScope;

public interface OperatorShipmentServicePort {

    boolean hasAnyShipment(final OperatorScope operatorScope);
}
