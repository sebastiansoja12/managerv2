package com.warehouse.shipment.infrastructure.adapter.secondary.exception;

import com.warehouse.exceptionhandler.exception.RestException;

public class ParcelNotFoundException extends RestException {

    private final static Integer errorCode = 404;

    public ParcelNotFoundException(String message) {
        super(errorCode, message);
    }
}
