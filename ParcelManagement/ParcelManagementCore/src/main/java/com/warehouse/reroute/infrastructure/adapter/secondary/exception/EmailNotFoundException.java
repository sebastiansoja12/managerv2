package com.warehouse.reroute.infrastructure.adapter.secondary.exception;

public class EmailNotFoundException extends RuntimeException {
    public EmailNotFoundException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public EmailNotFoundException(String exMessage) {
        super(exMessage);
    }
}
