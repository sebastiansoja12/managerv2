package com.warehouse.returntoken.domain.vo;

import com.warehouse.commonassets.identificator.SupplierCode;

public class CrossCourierDelivery {
    private final SupplierCode supplierCode;
    private final SupplierCode supplierCodeFromDevice;
    private final Boolean crossCourierDelivery;

    public CrossCourierDelivery(final SupplierCode supplierCode, final SupplierCode supplierCodeFromDevice) {
        this.supplierCode = supplierCode;
        this.supplierCodeFromDevice = supplierCodeFromDevice;
        this.crossCourierDelivery = !supplierCode.equals(supplierCodeFromDevice);
    }

    public SupplierCode getSupplierCode() {
        return supplierCode;
    }

    public SupplierCode getSupplierCodeFromDevice() {
        return supplierCodeFromDevice;
    }

    public Boolean isCrossCourierDelivery() {
        return crossCourierDelivery;
    }
}
