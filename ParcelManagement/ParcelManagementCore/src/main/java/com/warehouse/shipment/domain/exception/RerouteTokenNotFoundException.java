package com.warehouse.shipment.domain.exception;

public class RerouteTokenNotFoundException extends RuntimeException {

    public RerouteTokenNotFoundException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public RerouteTokenNotFoundException(String exMessage) {
        super(exMessage);
    }
}
