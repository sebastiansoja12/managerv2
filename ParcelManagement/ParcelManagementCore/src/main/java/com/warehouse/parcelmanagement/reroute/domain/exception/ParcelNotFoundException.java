package com.warehouse.parcelmanagement.reroute.domain.exception;

public class ParcelNotFoundException extends RuntimeException {
    public ParcelNotFoundException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public ParcelNotFoundException(String exMessage) {
        super(exMessage);
    }
}