package com.warehouse.auth.domain.model;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.OperatorId;

public record CreateOperatorUserCommand(
        OperatorId operatorId,
        String firstName,
        String lastName,
        String username,
        String password,
        String email,
        String role,
        DepartmentCode departmentCode,
        String language) {
}
