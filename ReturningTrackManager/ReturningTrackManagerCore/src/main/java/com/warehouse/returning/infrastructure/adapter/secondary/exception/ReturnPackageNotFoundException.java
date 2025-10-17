package com.warehouse.returning.infrastructure.adapter.secondary.exception;

public class ReturnPackageNotFoundException extends BusinessException {
    private final static int code = 404;
    private final static String exMessage = "Return package not found";
    public ReturnPackageNotFoundException() {
        super(code, exMessage);
    }
}
