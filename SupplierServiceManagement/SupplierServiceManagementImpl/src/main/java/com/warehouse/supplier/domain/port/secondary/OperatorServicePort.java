package com.warehouse.supplier.domain.port.secondary;

import com.warehouse.commonassets.identificator.OperatorId;

import java.util.Set;

public interface OperatorServicePort {
    Set<OperatorId> findActiveOperators();
}
