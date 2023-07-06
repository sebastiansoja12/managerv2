package com.warehouse.auth.domain.exception;

public class AuthenticationErrorException extends RuntimeException {
    public AuthenticationErrorException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public AuthenticationErrorException(String exMessage) {
        super(exMessage);
    }
}
