package com.warehouse.mcp.domain.vo;

import com.warehouse.commonassets.identificator.SupplierCode;

public record CourierChange(SupplierCode courierCode, String firstName, String lastName, String telephoneNumber) {

    public CourierChange applyTo(final Courier courier) {
        return new CourierChange(courierCode,
                firstName != null ? firstName : courier.firstName(),
                lastName != null ? lastName : courier.lastName(),
                telephoneNumber != null ? telephoneNumber : courier.telephoneNumber());
    }
}
