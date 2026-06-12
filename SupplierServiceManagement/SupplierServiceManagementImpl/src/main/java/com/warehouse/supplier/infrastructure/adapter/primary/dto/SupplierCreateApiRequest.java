package com.warehouse.supplier.infrastructure.adapter.primary.dto;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.SupplierCode;

public record SupplierCreateApiRequest(SupplierCode supplierCode,
                                       String firstName,
                                       String lastName,
                                       String telephoneNumber,
                                       DepartmentCode departmentCode) {

    public SupplierCreateApiRequest(final SupplierCode supplierCode,
                                    final String firstName,
                                    final String lastName,
                                    final String telephoneNumber) {
        this(supplierCode, firstName, lastName, telephoneNumber, null);
    }
}
