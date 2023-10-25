package com.warehouse.returning.domain.exception;

import com.warehouse.exception.RestException;

public class ReturnTokenMissingException extends RestException {

    public ReturnTokenMissingException(int code, String exMessage) {
        super(code, exMessage);
    }
}
