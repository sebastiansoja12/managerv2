package com.warehouse.auth.domain.exception;

public class ImpersonationConsentNotFoundException extends RuntimeException {

    public ImpersonationConsentNotFoundException(final Long consentId) {
        super("Impersonation consent " + consentId + " was not found");
    }
}
