package com.warehouse.supplier.domain.vo;

import com.warehouse.commonassets.identificator.SupplierCode;

public record SupplierUpdateResponse(SupplierCode supplierCode, Status status, String message) {

    public static SupplierUpdateResponse ok(final SupplierCode supplierCode) {
        return new SupplierUpdateResponse(supplierCode, Status.OK, "Supplier updated successfully.");
    }

    public static SupplierUpdateResponse failure(final SupplierCode supplierCode, final String message) {
        return new SupplierUpdateResponse(supplierCode, Status.FAILURE, message);
    }

    private enum Status {
        OK, FAILURE
    }
}
