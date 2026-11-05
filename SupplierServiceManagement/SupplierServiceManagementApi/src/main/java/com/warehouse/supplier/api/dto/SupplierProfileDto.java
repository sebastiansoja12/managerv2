package com.warehouse.supplier.api.dto;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.VehicleId;

public record SupplierProfileDto(
        SupplierId supplierId,
        SupplierCode supplierCode,
        String firstName,
        String lastName,
        DepartmentId departmentId,
        String status,
        VehicleId vehicleId) {
}
