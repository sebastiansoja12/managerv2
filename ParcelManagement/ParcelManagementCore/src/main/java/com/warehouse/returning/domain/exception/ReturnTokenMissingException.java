package com.warehouse.returning.domain.exception;

import com.warehouse.exceptionhandler.exception.RestException;

public class ReturnTokenMissingException extends RestException {

    public ReturnTokenMissingException(int code, String exMessage) {
        super(code, exMessage);
    }
}
