package com.warehouse.returning.infrastructure.adapter.secondary.exception;

import com.warehouse.exceptionhandler.exception.RestException;

public class ReturnPackageNotFoundException extends RestException {
    private final static int code = 404;
    private final static String exMessage = "Return package not found";
    public ReturnPackageNotFoundException() {
        super(code, exMessage);
    }
}
