package com.warehouse.returning.domain.exception;

import com.warehouse.exceptionhandler.exception.RestException;

public class StatusChangeException extends RestException {

    private static final int code = 400;

    public StatusChangeException(final String exMessage) {
        super(code, exMessage);
    }
}
