package com.warehouse.shipment.infrastructure.adapter.primary.exception;

import com.warehouse.exceptionhandler.exception.RestException;

public class EmptyRequestException extends RestException {

    private final static Integer errorCode = 400;

    public EmptyRequestException(final String exMessage) {
        super(errorCode, exMessage);
    }
}
