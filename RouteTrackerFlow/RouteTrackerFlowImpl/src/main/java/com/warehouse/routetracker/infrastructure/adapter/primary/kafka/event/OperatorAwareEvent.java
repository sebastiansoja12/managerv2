package com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event;

import com.warehouse.routetracker.domain.vo.identifier.DepartmentId;
import com.warehouse.routetracker.domain.vo.identifier.OperatorId;
import com.warehouse.routetracker.domain.vo.identifier.UserId;

public interface OperatorAwareEvent {

    OperatorId operatorId();

    DepartmentId departmentId();

    UserId userId();

    void assignOperatorId(final OperatorId operatorId);

    void assignOperatorContext(final OperatorId operatorId,
                               final UserId userId,
                               final DepartmentId departmentId);
}
