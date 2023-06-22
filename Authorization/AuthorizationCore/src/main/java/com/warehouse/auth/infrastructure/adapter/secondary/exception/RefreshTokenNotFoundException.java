package com.warehouse.auth.infrastructure.adapter.secondary.exception;

public class RefreshTokenNotFoundException extends RuntimeException {
    public RefreshTokenNotFoundException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public RefreshTokenNotFoundException(String exMessage) {
        super(exMessage);
    }
}
