package com.warehouse.returning.domain.exception;

import com.warehouse.exceptionhandler.exception.RestException;

public class DepotCodeMissingException extends RestException {
    public DepotCodeMissingException(int code, String exMessage) {
        super(code, exMessage);
    }
}
