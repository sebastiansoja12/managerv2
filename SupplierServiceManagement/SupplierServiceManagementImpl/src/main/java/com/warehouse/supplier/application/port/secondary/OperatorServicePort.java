package com.warehouse.supplier.application.port.secondary;

import com.warehouse.commonassets.identificator.OperatorId;

import java.util.Set;

public interface OperatorServicePort {
    Set<OperatorId> findActiveOperators();
}
