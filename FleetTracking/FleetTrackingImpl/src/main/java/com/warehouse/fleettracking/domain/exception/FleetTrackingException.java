package com.warehouse.fleettracking.domain.exception;

public class FleetTrackingException extends RuntimeException {

    public enum Reason { INVALID_REQUEST, ACCESS_DENIED, NOT_FOUND, CONFLICT }

    private final Reason reason;

    public FleetTrackingException(final Reason reason, final String message) {
        super(message);
        this.reason = reason;
    }

    public Reason reason() {
        return reason;
    }
}
