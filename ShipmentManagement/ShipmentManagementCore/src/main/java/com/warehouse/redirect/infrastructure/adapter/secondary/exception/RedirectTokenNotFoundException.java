package com.warehouse.redirect.infrastructure.adapter.secondary.exception;

public class RedirectTokenNotFoundException extends RuntimeException {
    public RedirectTokenNotFoundException(final String message) {
        super(message);
    }
}
