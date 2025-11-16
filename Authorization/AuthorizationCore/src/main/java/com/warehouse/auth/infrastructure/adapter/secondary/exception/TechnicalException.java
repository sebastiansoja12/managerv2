package com.warehouse.auth.infrastructure.adapter.secondary.exception;

public class TechnicalException extends RuntimeException {
    public TechnicalException(final String exMessage, final Exception exception) {
        super(exMessage, exception);
    }
}
