package com.warehouse.shipment.domain.exception;

import com.warehouse.exceptionhandler.exception.RestException;

public class ShipmentNotFoundException extends RestException {
    public ShipmentNotFoundException(int code, String message) {
        super(code, message);
    }
}
