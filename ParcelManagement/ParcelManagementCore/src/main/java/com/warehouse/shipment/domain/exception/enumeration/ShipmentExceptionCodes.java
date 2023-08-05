package com.warehouse.shipment.domain.exception.enumeration;

import lombok.Getter;

@Getter
public enum ShipmentExceptionCodes {

    SHIPMENT_201(201, "URL for payment has not been generated"),
    SHIPMENT_202(202, "Delivery depot could not be determined"),
    SHIPMENT_203(203, "Reroute is not possible"),
    SHIPMENT_204(204, "Parcel not found in request");

    private final int code;

    private final String message;

    ShipmentExceptionCodes(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
