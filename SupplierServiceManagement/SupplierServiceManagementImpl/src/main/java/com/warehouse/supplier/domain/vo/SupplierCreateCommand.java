package com.warehouse.supplier.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.SupplierCode;

public record SupplierCreateCommand(
        SupplierCode supplierCode,
        String firstName,
        String lastName,
        String telephoneNumber,
        DepartmentCode departmentCode) {

    public SupplierCreateCommand(final SupplierCode supplierCode,
                                 final String firstName,
                                 final String lastName,
                                 final String telephoneNumber) {
        this(supplierCode, firstName, lastName, telephoneNumber, null);
    }
}
