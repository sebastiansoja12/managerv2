package com.warehouse.commonassets.kafka.domain.model;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

public interface OperatorAwareEvent {

    OperatorId operatorId();

    DepartmentId departmentId();

    UserId userId();

    void assignOperatorId(final OperatorId operatorId);

    void assignOperatorContext(final OperatorId operatorId, final UserId userId, final DepartmentId departmentId);
}
