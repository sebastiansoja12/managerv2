package com.warehouse.supplier.api.dto;

public record CreateSupplierApiRequest(
        SupplierCodeDto supplierCode,
        String firstName,
        String lastName,
        String telephoneNumber,
        String departmentCode) {
}
