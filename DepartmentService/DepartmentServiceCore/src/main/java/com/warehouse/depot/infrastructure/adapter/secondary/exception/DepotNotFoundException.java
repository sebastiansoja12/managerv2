package com.warehouse.depot.infrastructure.adapter.secondary.exception;


public class DepotNotFoundException extends RuntimeException {
    public DepotNotFoundException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public DepotNotFoundException(String exMessage) {
        super(exMessage);
    }
}
