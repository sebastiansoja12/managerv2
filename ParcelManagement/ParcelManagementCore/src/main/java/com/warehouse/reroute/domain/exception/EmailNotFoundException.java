package com.warehouse.reroute.domain.exception;

public class EmailNotFoundException extends RuntimeException {
    public EmailNotFoundException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public EmailNotFoundException(String exMessage) {
        super(exMessage);
    }
}
