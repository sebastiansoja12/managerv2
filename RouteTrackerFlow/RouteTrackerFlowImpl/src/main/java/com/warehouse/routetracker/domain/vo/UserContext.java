package com.warehouse.routetracker.domain.vo;

import com.warehouse.routetracker.domain.vo.identifier.DepartmentId;
import com.warehouse.routetracker.domain.vo.identifier.OperatorId;
import com.warehouse.routetracker.domain.vo.identifier.UserId;

public record UserContext(UserId userId, OperatorId operatorId, DepartmentId departmentId) {
}
