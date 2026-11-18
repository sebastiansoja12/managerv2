package com.warehouse.auth.domain.model;

import com.warehouse.commonassets.identificator.DepartmentCode;

public record CreateUserCommand(
        String firstName,
        String lastName,
        String username,
        String email,
        String role,
        DepartmentCode departmentCode,
        String language) {
}
