package com.warehouse.account.application.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;

public record NewSupplierAccount(
        String username,
        String firstName,
        String lastName,
        String email,
        DepartmentId departmentId,
        String language) {
}
