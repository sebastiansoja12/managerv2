package com.warehouse.auth.domain.port.primary;

import com.warehouse.commonassets.identificator.OperatorId;

public interface OperatorSessionPort {

    long countActiveSessions(final OperatorId operatorId);

    void revokeSessions(final OperatorId operatorId);
}
