package com.warehouse.onboarding.infrastructure.adapter.secondary.entity;

import com.warehouse.onboarding.domain.enumeration.ConsentType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.Instant;

@Embeddable
public class UserConsentEmbeddable {

    @Enumerated(EnumType.STRING)
    @Column(name = "consent_type", nullable = false, length = 50)
    private ConsentType consentType;

    @Column(name = "consent_version", nullable = false, length = 50)
    private String consentVersion;

    @Column(name = "accepted_at", nullable = false)
    private Instant acceptedAt;

    protected UserConsentEmbeddable() {
    }

    public UserConsentEmbeddable(final ConsentType consentType, final String consentVersion, final Instant acceptedAt) {
        this.consentType = consentType;
        this.consentVersion = consentVersion;
        this.acceptedAt = acceptedAt;
    }

    public ConsentType getConsentType() {
        return consentType;
    }

    public String getConsentVersion() {
        return consentVersion;
    }

    public Instant getAcceptedAt() {
        return acceptedAt;
    }
}
