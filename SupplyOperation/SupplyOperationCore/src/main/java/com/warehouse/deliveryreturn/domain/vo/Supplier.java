package com.warehouse.deliveryreturn.domain.vo;

public class Supplier {
    private final String supplierCode;
    private final Boolean active;

    public Supplier(final String supplierCode, final Boolean active) {
        this.supplierCode = supplierCode;
        this.active = active;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public boolean isActive() {
        return active;
    }
}
