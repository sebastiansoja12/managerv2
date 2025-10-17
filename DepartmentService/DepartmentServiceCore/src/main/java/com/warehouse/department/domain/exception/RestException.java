package com.warehouse.department.domain.exception;

public class RestException extends RuntimeException {
    public RestException(final String message) {
        super(message);
    }
}
