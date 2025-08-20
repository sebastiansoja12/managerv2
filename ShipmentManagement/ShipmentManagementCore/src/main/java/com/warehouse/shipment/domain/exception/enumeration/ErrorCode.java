package com.warehouse.shipment.domain.exception.enumeration;

import lombok.Getter;

@Getter
public enum ErrorCode {

    SHIPMENT_201(401, "URL for payment has not been generated"),
    SHIPMENT_202(402, "Delivery department could not be determined"),
    SHIPMENT_203(403, "Reroute is not possible"),
    SHIPMENT_204(404, "Shipment not found in request"),

    ORIGIN_DEPARTMENT_NOT_AVAILABLE(400, "Origin department is not available"),
    DESTINATION_DEPARTMENT_NOT_AVAILABLE(400, "Destination department is not available");

    private final int code;

    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
