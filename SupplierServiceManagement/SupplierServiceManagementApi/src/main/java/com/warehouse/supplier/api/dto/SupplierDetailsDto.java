package com.warehouse.supplier.api.dto;

public record SupplierDetailsDto(
        Long supplierId,
        SupplierCodeDto supplierCode,
        String firstName,
        String lastName,
        String telephoneNumber,
        String departmentCode,
        String status) {
}
