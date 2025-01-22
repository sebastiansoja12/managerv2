package com.warehouse.returntoken.infrastructure.adapter.primary.dto;

import org.apache.commons.lang3.ObjectUtils;

public record CrossCourierDeliveryDto(SupplierCodeDto supplierCode, SupplierCodeDto supplierCodeFromDevice) {
    public boolean isCrossCourierDelivery() {
        return ObjectUtils.notEqual(supplierCodeFromDevice, supplierCode);
    }
}
