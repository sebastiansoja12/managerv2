package com.warehouse.returntoken.domain.vo;

import com.warehouse.commonassets.identificator.SupplierCode;

public class Supplier {
    private final SupplierCode supplierCode;

    public Supplier(final SupplierCode supplierCode) {
        this.supplierCode = supplierCode;
    }

    public SupplierCode getSupplierCode() {
        return supplierCode;
    }
}
