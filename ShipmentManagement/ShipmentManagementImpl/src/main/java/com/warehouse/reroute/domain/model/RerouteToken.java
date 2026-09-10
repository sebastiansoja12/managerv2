package com.warehouse.reroute.domain.model;

import java.time.Instant;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.reroute.domain.enumeration.RerouteTokenVerification;
import com.warehouse.reroute.domain.exception.RerouteTokenExpiredException;
import com.warehouse.reroute.domain.vo.RerouteTokenId;
import com.warehouse.reroute.domain.vo.RerouteTokenPolicy;
import com.warehouse.reroute.domain.vo.RerouteTokenValue;

public class RerouteToken {

    private final RerouteTokenId rerouteTokenId;
    private final ShipmentId shipmentId;
    private final RerouteTokenValue value;
    private final String email;
    private final Instant createdAt;
    private Instant expiresAt;
    private int failedAttempts;
    private Instant usedAt;

    public RerouteToken(final RerouteTokenId rerouteTokenId,
                        final ShipmentId shipmentId,
                        final RerouteTokenValue value,
                        final String email,
                        final Instant createdAt,
                        final Instant expiresAt,
                        final int failedAttempts,
                        final Instant usedAt) {
        this.rerouteTokenId = rerouteTokenId;
        this.shipmentId = shipmentId;
        this.value = value;
        this.email = email;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.failedAttempts = failedAttempts;
        this.usedAt = usedAt;
    }

    public static RerouteToken issue(final ShipmentId shipmentId,
                                     final String email,
                                     final RerouteTokenValue value,
                                     final Instant now,
                                     final RerouteTokenPolicy policy) {
        return new RerouteToken(null, shipmentId, value, email, now, now.plus(policy.validity()), 0, null);
    }

    public boolean isActive(final Instant now) {
        return usedAt == null && expiresAt.isAfter(now);
    }

    public RerouteTokenVerification verify(final RerouteTokenValue providedValue,
                                           final Instant now,
                                           final RerouteTokenPolicy policy) {
        if (!isActive(now)) {
            return RerouteTokenVerification.EXPIRED;
        }
        if (value.equals(providedValue)) {
            return RerouteTokenVerification.VALID;
        }
        failedAttempts++;
        if (failedAttempts >= policy.maxFailedAttempts()) {
            expiresAt = now;
            return RerouteTokenVerification.ATTEMPTS_EXCEEDED;
        }
        return RerouteTokenVerification.INVALID;
    }

    public void markUsed(final Instant now) {
        if (!isActive(now)) {
            throw new RerouteTokenExpiredException();
        }
        usedAt = now;
    }

    public void revoke(final Instant now) {
        if (isActive(now)) {
            expiresAt = now;
        }
    }

    public int remainingAttempts(final RerouteTokenPolicy policy) {
        return Math.max(0, policy.maxFailedAttempts() - failedAttempts);
    }

    public RerouteTokenId getRerouteTokenId() {
        return rerouteTokenId;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public RerouteTokenValue getValue() {
        return value;
    }

    public String getEmail() {
        return email;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public Instant getUsedAt() {
        return usedAt;
    }
}
