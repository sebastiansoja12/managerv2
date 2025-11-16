package com.warehouse.auth.infrastructure.adapter.secondary.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(final String exMessage, final Exception exception) {
        super(exMessage, exception);
    }

    public BusinessException(final String exMessage) {
        super(exMessage);
    }
}
