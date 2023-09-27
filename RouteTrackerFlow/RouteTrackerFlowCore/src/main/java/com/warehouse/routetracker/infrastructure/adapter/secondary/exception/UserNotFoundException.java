package com.warehouse.routetracker.infrastructure.adapter.secondary.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public UserNotFoundException(String exMessage) {
        super(exMessage);
    }
}
