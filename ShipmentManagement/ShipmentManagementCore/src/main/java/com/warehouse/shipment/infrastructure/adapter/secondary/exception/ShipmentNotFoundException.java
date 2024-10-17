package com.warehouse.shipment.infrastructure.adapter.secondary.exception;

import com.warehouse.exceptionhandler.exception.RestException;

public class ShipmentNotFoundException extends RestException {

    private final static Integer errorCode = 404;

    public ShipmentNotFoundException(String message) {
        super(errorCode, message);
    }
}
