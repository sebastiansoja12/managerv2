package com.warehouse.auth.domain.port.primary;

import com.warehouse.commonassets.identificator.OperatorId;

public interface CurrentOperatorPort {
    OperatorId getCurrentOperatorId();
}
