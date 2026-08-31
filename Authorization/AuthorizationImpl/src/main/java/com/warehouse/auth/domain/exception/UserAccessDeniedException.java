package com.warehouse.auth.domain.exception;

public class UserAccessDeniedException extends RuntimeException {

    public UserAccessDeniedException(final String message) {
        super(message);
    }
}
