package com.warehouse.returning.domain.exception;

import com.warehouse.exceptionhandler.exception.RestException;

public class UsernameMissingException extends RestException {
    public UsernameMissingException(int code, String exMessage) {
        super(code, exMessage);
    }
}
