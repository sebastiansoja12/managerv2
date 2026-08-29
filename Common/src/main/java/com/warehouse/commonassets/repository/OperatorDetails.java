package com.warehouse.commonassets.repository;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

public record OperatorDetails(OperatorId operatorId,
                              UserId userId,
                              DepartmentId departmentId) {
}
