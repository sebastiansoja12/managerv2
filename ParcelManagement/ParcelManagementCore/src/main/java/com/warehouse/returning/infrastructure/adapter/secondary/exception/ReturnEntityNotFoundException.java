package com.warehouse.returning.infrastructure.adapter.secondary.exception;

import com.warehouse.exceptionhandler.exception.RestException;

public class ReturnEntityNotFoundException extends RestException {
    public ReturnEntityNotFoundException(int code, String exMessage) {
        super(code, exMessage);
    }
}
