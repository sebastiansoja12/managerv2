package com.warehouse.reroute.domain.exception;

public class RerouteException extends RuntimeException {
    public RerouteException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public RerouteException(String exMessage) {
        super(exMessage);
    }
}
