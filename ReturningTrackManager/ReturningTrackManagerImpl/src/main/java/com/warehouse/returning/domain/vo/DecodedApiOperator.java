package com.warehouse.returning.domain.vo;

import com.warehouse.returning.domain.vo.DepartmentId;
import com.warehouse.returning.domain.vo.OperatorId;

public record DecodedApiOperator(UserId userId, DepartmentId departmentId, OperatorId operatorId, String username) {
}
