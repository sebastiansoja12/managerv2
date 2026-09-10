package com.warehouse.commonassets.event.integration.context;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

public abstract class OperatorAwareContext implements OperatorAwareEvent {

    private UserId userId;
    private DepartmentId departmentId;
    private OperatorId operatorId;

    protected OperatorAwareContext() {
    }

    @Override
    public void assignOperatorContext(final OperatorId operatorId,
                                      final UserId userId,
                                      final DepartmentId departmentId) {
        this.userId = userId;
        this.departmentId = departmentId;
        this.operatorId = operatorId;
    }

    @Override
    public void assignOperatorId(final OperatorId operatorId) {
        this.operatorId = operatorId;
    }

    @Override
    public UserId userId() {
        return userId;
    }

    @Override
    @JsonIgnore
    public DepartmentId departmentId() {
        return departmentId;
    }

    @Override
    public OperatorId operatorId() {
        return operatorId;
    }
}
