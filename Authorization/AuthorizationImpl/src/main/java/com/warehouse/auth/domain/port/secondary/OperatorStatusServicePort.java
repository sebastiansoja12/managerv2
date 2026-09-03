package com.warehouse.auth.domain.port.secondary;

import com.warehouse.commonassets.identificator.OperatorId;

public interface OperatorStatusServicePort {

    boolean isActive(final OperatorId operatorId);
}
