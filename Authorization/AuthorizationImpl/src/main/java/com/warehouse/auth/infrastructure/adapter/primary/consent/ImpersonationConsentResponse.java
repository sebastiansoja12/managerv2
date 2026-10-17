package com.warehouse.auth.infrastructure.adapter.primary.consent;

import com.warehouse.auth.domain.model.ImpersonationConsent;

import java.time.Instant;

public record ImpersonationConsentResponse(Long id,
                                           Long operatorId,
                                           Long platformOwnerId,
                                           String platformOwnerUsername,
                                           String reason,
                                           String status,
                                           Instant requestedAt,
                                           Instant requestExpiresAt,
                                           String decidedByUsername,
                                           Instant decidedAt,
                                           Instant accessExpiresAt) {

    public static ImpersonationConsentResponse from(final ImpersonationConsent consent, final Instant now) {
        return new ImpersonationConsentResponse(consent.getId(), consent.getOperatorId().value(),
                consent.getPlatformOwnerId().value(), consent.getPlatformOwnerUsername(), consent.getReason(),
                consent.statusAt(now).name(), consent.getRequestedAt(), consent.getRequestExpiresAt(),
                consent.getDecidedByUsername(), consent.getDecidedAt(), consent.getAccessExpiresAt());
    }
}
