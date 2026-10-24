package com.warehouse.account.domain.model;

import com.warehouse.account.domain.enumeration.UserInvitationFailure;
import com.warehouse.account.domain.exception.InvalidUserInvitationException;
import com.warehouse.account.domain.vo.UserInvitationId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;

import java.time.Duration;
import java.time.Instant;

public class UserInvitation {

    public static final int MIN_PASSWORD_LENGTH = 8;

    private final UserInvitationId invitationId;
    private final UserId userId;
    private final SupplierId supplierId;
    private final String username;
    private final String tokenHash;
    private final Instant createdAt;
    private final Instant expiresAt;
    private Instant acceptedAt;
    private final OperatorId operatorId;

    public UserInvitation(final UserInvitationId invitationId,
                          final UserId userId,
                          final SupplierId supplierId,
                          final String username,
                          final String tokenHash,
                          final Instant createdAt,
                          final Instant expiresAt,
                          final Instant acceptedAt,
                          final OperatorId operatorId) {
        this.invitationId = invitationId;
        this.userId = userId;
        this.supplierId = supplierId;
        this.username = username;
        this.tokenHash = tokenHash;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.acceptedAt = acceptedAt;
        this.operatorId = operatorId;
    }

    public static UserInvitation issue(final UserId userId,
                                       final SupplierId supplierId,
                                       final String username,
                                       final String tokenHash,
                                       final Instant timestamp,
                                       final Duration validity) {
        return new UserInvitation(UserInvitationId.generate(), userId, supplierId, username, tokenHash, timestamp,
                timestamp.plus(validity), null, null);
    }

    public void ensureUsable(final Instant timestamp) {
        if (acceptedAt != null) {
            throw new InvalidUserInvitationException(UserInvitationFailure.ALREADY_ACCEPTED);
        }
        if (!timestamp.isBefore(expiresAt)) {
            throw new InvalidUserInvitationException(UserInvitationFailure.EXPIRED);
        }
    }

    public void accept(final String password, final Instant timestamp) {
        ensureUsable(timestamp);
        if (password == null || password.length() < MIN_PASSWORD_LENGTH || password.isBlank()) {
            throw new InvalidUserInvitationException(UserInvitationFailure.WEAK_PASSWORD);
        }
        this.acceptedAt = timestamp;
    }

    public UserInvitationId getInvitationId() {
        return invitationId;
    }

    public UserId getUserId() {
        return userId;
    }

    public SupplierId getSupplierId() {
        return supplierId;
    }

    public String getUsername() {
        return username;
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public Instant getAcceptedAt() {
        return acceptedAt;
    }

    public OperatorId getOperatorId() {
        return operatorId;
    }
}
