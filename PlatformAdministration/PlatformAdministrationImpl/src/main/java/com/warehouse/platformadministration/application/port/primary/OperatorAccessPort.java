package com.warehouse.platformadministration.application.port.primary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.platformadministration.domain.vo.OperatorAccess;

public interface OperatorAccessPort {

    OperatorAccess findAccess(final OperatorId operatorId);

    void revokeSessions(final OperatorId operatorId);
}
