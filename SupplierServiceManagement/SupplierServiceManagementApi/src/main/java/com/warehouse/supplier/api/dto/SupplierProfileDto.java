package com.warehouse.supplier.api.dto;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;

public record SupplierProfileDto(
        SupplierId supplierId,
        SupplierCode supplierCode,
        String firstName,
        String lastName,
        DepartmentId departmentId,
        String status) {
}
