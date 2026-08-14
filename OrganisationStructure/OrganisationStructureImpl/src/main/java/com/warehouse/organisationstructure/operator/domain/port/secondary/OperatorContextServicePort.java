package com.warehouse.organisationstructure.operator.domain.port.secondary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

public interface OperatorContextServicePort {

    void runInContext(final OperatorId operatorId, final Runnable operation);

    void runInContext(final OperatorId operatorId, final UserId userId, final Runnable operation);
}
