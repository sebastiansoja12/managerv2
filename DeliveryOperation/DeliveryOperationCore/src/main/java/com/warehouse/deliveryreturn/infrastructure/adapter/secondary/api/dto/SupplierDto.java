package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto;

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
