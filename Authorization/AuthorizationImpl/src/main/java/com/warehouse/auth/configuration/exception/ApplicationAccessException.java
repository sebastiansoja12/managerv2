package com.warehouse.auth.configuration.exception;

import com.warehouse.exceptionhandler.exception.RestException;

public class ApplicationAccessException extends RestException {
    public ApplicationAccessException(int code, String exMessage) {
        super(code, exMessage);
    }
}
