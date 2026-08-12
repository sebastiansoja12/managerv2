package com.warehouse.commonassets.kafka.domain.model;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

public abstract class OperatorAwareContext implements OperatorAwareEvent {

    private UserId userId;
    private DepartmentId departmentId;
    private OperatorId operatorId;

    protected OperatorAwareContext() {
    }

    protected OperatorAwareContext(final UserId userId,
                                   final DepartmentId departmentId,
                                   final OperatorId operatorId) {
        this.userId = userId;
        this.departmentId = departmentId;
        this.operatorId = operatorId;
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

    public UserId getUserId() {
        return userId;
    }

    public DepartmentId getDepartmentId() {
        return departmentId;
    }

    public OperatorId getOperatorId() {
        return operatorId;
    }

    @Override
    public UserId userId() {
        return userId;
    }

    @Override
    public DepartmentId departmentId() {
        return departmentId;
    }

    @Override
    public OperatorId operatorId() {
        return operatorId;
    }
}
