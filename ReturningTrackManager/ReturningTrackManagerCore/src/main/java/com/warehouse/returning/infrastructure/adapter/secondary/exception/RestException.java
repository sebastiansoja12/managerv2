package com.warehouse.returning.infrastructure.adapter.secondary.exception;

public class RestException extends RuntimeException {
    private final int code;
    private final String message;

    public RestException(final int code, final String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
}
