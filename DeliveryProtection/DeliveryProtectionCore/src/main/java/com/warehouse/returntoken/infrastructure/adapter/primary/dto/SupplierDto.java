package com.warehouse.returntoken.infrastructure.adapter.primary.dto;

public class SupplierDto {
    private SupplierCodeDto supplierCode;

    public SupplierDto() {
    }

    public SupplierDto(final SupplierCodeDto supplierCode) {
        this.supplierCode = supplierCode;
    }

    public SupplierCodeDto getSupplierCode() {
        return supplierCode;
    }
}
