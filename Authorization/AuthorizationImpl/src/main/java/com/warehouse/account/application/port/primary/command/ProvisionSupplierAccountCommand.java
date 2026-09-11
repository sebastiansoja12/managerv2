package com.warehouse.account.application.port.primary.command;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;

public record ProvisionSupplierAccountCommand(
        SupplierId supplierId,
        SupplierCode supplierCode,
        String firstName,
        String lastName,
        String email,
        DepartmentId departmentId) {
}
