package com.warehouse.returning.domain.vo;

import com.warehouse.common.DepartmentId;
import com.warehouse.common.OperatorId;

public record DecodedApiOperator(UserId userId, DepartmentId departmentId, OperatorId operatorId, String username) {
}
