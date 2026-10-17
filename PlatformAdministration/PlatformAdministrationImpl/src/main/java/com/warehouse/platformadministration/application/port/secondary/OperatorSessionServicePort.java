package com.warehouse.platformadministration.application.port.secondary;

import com.warehouse.commonassets.identificator.OperatorId;

public interface OperatorSessionServicePort {

    long countActiveSessions(final OperatorId operatorId);

    void revokeSessions(final OperatorId operatorId);
}
