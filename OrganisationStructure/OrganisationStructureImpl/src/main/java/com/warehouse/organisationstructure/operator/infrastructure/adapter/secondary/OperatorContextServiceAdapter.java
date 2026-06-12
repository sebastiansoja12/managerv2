package com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.organisationstructure.operator.domain.port.secondary.OperatorContextServicePort;

public class OperatorContextServiceAdapter implements OperatorContextServicePort {

    private final OperatorContext operatorContext;

    public OperatorContextServiceAdapter(final OperatorContext operatorContext) {
        this.operatorContext = operatorContext;
    }

    @Override
    public void runInContext(final OperatorId operatorId, final Runnable operation) {
        operatorContext.runAs(operatorId, operation);
    }

    @Override
    public void runInContext(final OperatorId operatorId, final UserId userId, final Runnable operation) {
        operatorContext.runAs(operatorId, userId, operation);
    }
}
