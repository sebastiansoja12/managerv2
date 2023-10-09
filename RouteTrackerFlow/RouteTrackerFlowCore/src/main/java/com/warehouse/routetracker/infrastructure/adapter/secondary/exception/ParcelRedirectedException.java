package com.warehouse.routetracker.infrastructure.adapter.secondary.exception;

public class ParcelRedirectedException extends RuntimeException {
    public ParcelRedirectedException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public ParcelRedirectedException(String exMessage) {
        super(exMessage);
    }
}