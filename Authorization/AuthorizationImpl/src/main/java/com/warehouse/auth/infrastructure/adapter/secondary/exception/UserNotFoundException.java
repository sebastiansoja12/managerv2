package com.warehouse.auth.infrastructure.adapter.secondary.exception;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException(final String exMessage, final Exception exception) {
        super(exMessage, exception);
    }

    public UserNotFoundException(final String exMessage) {
        super(exMessage);
    }
}

