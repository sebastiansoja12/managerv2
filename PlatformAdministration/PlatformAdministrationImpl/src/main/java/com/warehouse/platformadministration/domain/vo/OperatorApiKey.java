package com.warehouse.platformadministration.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;

public record OperatorApiKey(UserId userId,
                             String username,
                             String firstName,
                             String lastName,
                             String role,
                             DepartmentCode departmentCode) {
}
