package com.warehouse.returning.infrastructure.adapter.secondary.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(final String message) {
        super(message);
    }
}
