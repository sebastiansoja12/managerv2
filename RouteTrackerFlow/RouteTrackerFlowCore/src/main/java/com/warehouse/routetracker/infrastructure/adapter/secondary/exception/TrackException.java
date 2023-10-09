package com.warehouse.routetracker.infrastructure.adapter.secondary.exception;

public class TrackException extends RuntimeException {
    public TrackException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public TrackException(String exMessage) {
        super(exMessage);
    }
}
