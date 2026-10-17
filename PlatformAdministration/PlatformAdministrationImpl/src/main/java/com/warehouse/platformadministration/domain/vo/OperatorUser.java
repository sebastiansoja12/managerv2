package com.warehouse.platformadministration.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;

public record OperatorUser(UserId userId,
                           String username,
                           String firstName,
                           String lastName,
                           String email,
                           String role,
                           DepartmentCode departmentCode,
                           boolean deleted) {
}
