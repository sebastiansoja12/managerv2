package com.warehouse.onboarding.domain.exception;

import com.warehouse.onboarding.domain.enumeration.ConsentType;

import java.util.Set;

public class MissingRequiredConsentException extends RuntimeException {

    private final Set<ConsentType> missingConsents;

    public MissingRequiredConsentException(final Set<ConsentType> missingConsents) {
        super("Required consents are missing: " + missingConsents);
        this.missingConsents = missingConsents;
    }

    public Set<ConsentType> getMissingConsents() {
        return missingConsents;
    }
}
