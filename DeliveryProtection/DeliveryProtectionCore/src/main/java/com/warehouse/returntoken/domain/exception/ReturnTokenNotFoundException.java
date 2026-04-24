package com.warehouse.returntoken.domain.exception;

public class ReturnTokenNotFoundException extends RuntimeException {
    public ReturnTokenNotFoundException(final String message) {
        super(message);
    }
}
