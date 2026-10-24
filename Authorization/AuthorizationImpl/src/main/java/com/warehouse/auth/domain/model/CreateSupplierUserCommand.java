package com.warehouse.auth.domain.model;

import com.warehouse.commonassets.identificator.DepartmentId;

public record CreateSupplierUserCommand(
        String username,
        String firstName,
        String lastName,
        String email,
        DepartmentId departmentId,
        String language) {
}
