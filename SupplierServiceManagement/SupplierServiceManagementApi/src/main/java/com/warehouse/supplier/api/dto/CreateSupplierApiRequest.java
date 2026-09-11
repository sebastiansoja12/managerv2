package com.warehouse.supplier.api.dto;

public record CreateSupplierApiRequest(
        SupplierCodeDto supplierCode,
        String firstName,
        String lastName,
        String telephoneNumber,
        String departmentCode,
        String email) {

    public CreateSupplierApiRequest(final SupplierCodeDto supplierCode,
                                    final String firstName,
                                    final String lastName,
                                    final String telephoneNumber,
                                    final String departmentCode) {
        this(supplierCode, firstName, lastName, telephoneNumber, departmentCode, null);
    }
}
