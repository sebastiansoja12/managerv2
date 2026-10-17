package com.warehouse.auth.domain.model;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PlatformOwnerId;
import com.warehouse.commonassets.identificator.UserId;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;

public final class ImpersonationConsent {

    public static final Duration REQUEST_VALIDITY = Duration.ofHours(24);

    public static final Set<Integer> ALLOWED_DURATION_MINUTES = Set.of(30, 60, 120, 240, 480);

    public static final int MAX_REASON_LENGTH = 500;

    private final Long id;

    private final OperatorId operatorId;

    private final PlatformOwnerId platformOwnerId;

    private final String platformOwnerUsername;

    private final String reason;

    private final ImpersonationConsentStatus status;

    private final Instant requestedAt;

    private final Instant requestExpiresAt;

    private final UserId decidedBy;

    private final String decidedByUsername;

    private final Instant decidedAt;

    private final Instant accessExpiresAt;

    private ImpersonationConsent(final Long id,
                                 final OperatorId operatorId,
                                 final PlatformOwnerId platformOwnerId,
                                 final String platformOwnerUsername,
                                 final String reason,
                                 final ImpersonationConsentStatus status,
                                 final Instant requestedAt,
                                 final Instant requestExpiresAt,
                                 final UserId decidedBy,
                                 final String decidedByUsername,
                                 final Instant decidedAt,
                                 final Instant accessExpiresAt) {
        this.id = id;
        this.operatorId = Objects.requireNonNull(operatorId, "Operator must not be null");
        this.platformOwnerId = Objects.requireNonNull(platformOwnerId, "Platform owner must not be null");
        this.platformOwnerUsername = platformOwnerUsername;
        this.reason = reason;
        this.status = Objects.requireNonNull(status, "Status must not be null");
        this.requestedAt = Objects.requireNonNull(requestedAt, "Requested at must not be null");
        this.requestExpiresAt = Objects.requireNonNull(requestExpiresAt, "Request expiry must not be null");
        this.decidedBy = decidedBy;
        this.decidedByUsername = decidedByUsername;
        this.decidedAt = decidedAt;
        this.accessExpiresAt = accessExpiresAt;
    }

    public static ImpersonationConsent request(final OperatorId operatorId,
                                               final PlatformOwnerId platformOwnerId,
                                               final String platformOwnerUsername,
                                               final String reason,
                                               final Instant now) {
        return new ImpersonationConsent(null, operatorId, platformOwnerId, platformOwnerUsername,
                normalizeReason(reason), ImpersonationConsentStatus.PENDING, now, now.plus(REQUEST_VALIDITY), null,
                null, null, null);
    }

    public static ImpersonationConsent restore(final Long id,
                                               final OperatorId operatorId,
                                               final PlatformOwnerId platformOwnerId,
                                               final String platformOwnerUsername,
                                               final String reason,
                                               final ImpersonationConsentStatus status,
                                               final Instant requestedAt,
                                               final Instant requestExpiresAt,
                                               final UserId decidedBy,
                                               final String decidedByUsername,
                                               final Instant decidedAt,
                                               final Instant accessExpiresAt) {
        return new ImpersonationConsent(id, operatorId, platformOwnerId, platformOwnerUsername, reason, status,
                requestedAt, requestExpiresAt, decidedBy, decidedByUsername, decidedAt, accessExpiresAt);
    }

    public ImpersonationConsent approve(final UserId userId,
                                        final String username,
                                        final int durationMinutes,
                                        final Instant now) {
        requireStatus(ImpersonationConsentStatus.PENDING, now);
        if (!ALLOWED_DURATION_MINUTES.contains(durationMinutes)) {
            throw new IllegalArgumentException("Access duration must be one of " + ALLOWED_DURATION_MINUTES);
        }
        return decide(ImpersonationConsentStatus.APPROVED, userId, username, now,
                now.plus(Duration.ofMinutes(durationMinutes)));
    }

    public ImpersonationConsent reject(final UserId userId, final String username, final Instant now) {
        requireStatus(ImpersonationConsentStatus.PENDING, now);
        return decide(ImpersonationConsentStatus.REJECTED, userId, username, now, null);
    }

    public ImpersonationConsent revoke(final UserId userId, final String username, final Instant now) {
        requireStatus(ImpersonationConsentStatus.APPROVED, now);
        return decide(ImpersonationConsentStatus.REVOKED, userId, username, now, accessExpiresAt);
    }

    public ImpersonationConsentStatus statusAt(final Instant now) {
        if (status == ImpersonationConsentStatus.PENDING && !now.isBefore(requestExpiresAt)) {
            return ImpersonationConsentStatus.EXPIRED;
        }
        if (status == ImpersonationConsentStatus.APPROVED
                && (accessExpiresAt == null || !now.isBefore(accessExpiresAt))) {
            return ImpersonationConsentStatus.EXPIRED;
        }
        return status;
    }

    public boolean grantsAccess(final Instant now) {
        return statusAt(now) == ImpersonationConsentStatus.APPROVED;
    }

    public boolean isOpen(final Instant now) {
        final ImpersonationConsentStatus currentStatus = statusAt(now);
        return currentStatus == ImpersonationConsentStatus.PENDING || currentStatus == ImpersonationConsentStatus.APPROVED;
    }

    public boolean belongsTo(final PlatformOwnerId owner, final OperatorId operator) {
        return platformOwnerId.equals(owner) && operatorId.equals(operator);
    }

    private ImpersonationConsent decide(final ImpersonationConsentStatus nextStatus,
                                        final UserId userId,
                                        final String username,
                                        final Instant now,
                                        final Instant nextAccessExpiresAt) {
        return new ImpersonationConsent(id, operatorId, platformOwnerId, platformOwnerUsername, reason, nextStatus,
                requestedAt, requestExpiresAt, userId, username, now, nextAccessExpiresAt);
    }

    private void requireStatus(final ImpersonationConsentStatus expected, final Instant now) {
        final ImpersonationConsentStatus currentStatus = statusAt(now);
        if (currentStatus != expected) {
            throw new IllegalStateException("Impersonation consent is " + currentStatus + ", expected " + expected);
        }
    }

    private static String normalizeReason(final String reason) {
        if (reason == null || reason.isBlank()) {
            return null;
        }
        final String normalized = reason.trim();
        if (normalized.length() > MAX_REASON_LENGTH) {
            throw new IllegalArgumentException("Reason cannot be longer than " + MAX_REASON_LENGTH);
        }
        return normalized;
    }

    public Long getId() {
        return id;
    }

    public OperatorId getOperatorId() {
        return operatorId;
    }

    public PlatformOwnerId getPlatformOwnerId() {
        return platformOwnerId;
    }

    public String getPlatformOwnerUsername() {
        return platformOwnerUsername;
    }

    public String getReason() {
        return reason;
    }

    public ImpersonationConsentStatus getStatus() {
        return status;
    }

    public Instant getRequestedAt() {
        return requestedAt;
    }

    public Instant getRequestExpiresAt() {
        return requestExpiresAt;
    }

    public UserId getDecidedBy() {
        return decidedBy;
    }

    public String getDecidedByUsername() {
        return decidedByUsername;
    }

    public Instant getDecidedAt() {
        return decidedAt;
    }

    public Instant getAccessExpiresAt() {
        return accessExpiresAt;
    }
}
