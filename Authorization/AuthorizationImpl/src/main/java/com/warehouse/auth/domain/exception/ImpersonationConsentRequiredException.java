package com.warehouse.auth.domain.exception;

public class ImpersonationConsentRequiredException extends RuntimeException {

    public static final String NONE = "NONE";

    private final String reason;

    public ImpersonationConsentRequiredException(final String reason) {
        super("Operator has not granted access for impersonation (" + reason + ")");
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
