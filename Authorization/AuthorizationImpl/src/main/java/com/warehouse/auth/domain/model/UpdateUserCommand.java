package com.warehouse.auth.domain.model;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;

public record UpdateUserCommand(
        UserId userId,
        String firstName,
        String lastName,
        String username,
        String email,
        DepartmentCode departmentCode,
        String language) {
}
