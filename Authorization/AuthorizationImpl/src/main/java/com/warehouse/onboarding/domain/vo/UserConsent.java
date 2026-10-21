package com.warehouse.onboarding.domain.vo;

import com.warehouse.onboarding.domain.enumeration.ConsentType;

import java.time.Instant;

public record UserConsent(ConsentType type, ConsentVersion version, Instant acceptedAt) {

    public boolean covers(final ConsentType consentType, final ConsentVersion consentVersion) {
        return type == consentType && version.equals(consentVersion);
    }
}
