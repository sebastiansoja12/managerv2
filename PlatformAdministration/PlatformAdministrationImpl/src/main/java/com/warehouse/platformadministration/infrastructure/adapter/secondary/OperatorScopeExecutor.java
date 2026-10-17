package com.warehouse.platformadministration.infrastructure.adapter.secondary;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.platformadministration.domain.vo.OperatorScope;

import java.util.function.Supplier;

public class OperatorScopeExecutor {

    private final OperatorContext operatorContext;

    public OperatorScopeExecutor(final OperatorContext operatorContext) {
        this.operatorContext = operatorContext;
    }

    public <T> T execute(final OperatorScope operatorScope, final Supplier<T> operation) {
        if (operatorScope.actingUserId() == null) {
            return operatorContext.runAs(operatorScope.operatorId(), operation);
        }
        return operatorContext.runAs(operatorScope.operatorId(), operatorScope.actingUserId(), operation);
    }
}
