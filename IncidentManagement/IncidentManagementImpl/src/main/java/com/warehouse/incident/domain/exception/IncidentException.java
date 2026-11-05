package com.warehouse.incident.domain.exception;

public class IncidentException extends RuntimeException {

    public enum Reason { INVALID_REQUEST, ACCESS_DENIED, NOT_FOUND, CONFLICT }

    private final Reason reason;

    public IncidentException(final Reason reason, final String message) {
        super(message);
        this.reason = reason;
    }

    public Reason reason() {
        return reason;
    }
}
