package com.warehouse.account.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.BelongsToOperator;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity(name = "account.UserInvitationEntity")
@Table(name = "user_invitations")
public class UserInvitationEntity extends BelongsToOperator {

    @Id
    @Column(name = "invitation_id", nullable = false)
    private UUID invitationId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "user_id", nullable = false))
    private UserId userId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "supplier_id"))
    private SupplierId supplierId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "token_hash", nullable = false, length = 64)
    private String tokenHash;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "accepted_at")
    private Instant acceptedAt;

    protected UserInvitationEntity() {
    }

    public UserInvitationEntity(final UUID invitationId,
                                final UserId userId,
                                final SupplierId supplierId,
                                final String username,
                                final String tokenHash,
                                final Instant createdAt,
                                final Instant expiresAt,
                                final Instant acceptedAt) {
        this.invitationId = invitationId;
        this.userId = userId;
        this.supplierId = supplierId;
        this.username = username;
        this.tokenHash = tokenHash;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.acceptedAt = acceptedAt;
    }

    public UUID getInvitationId() {
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
}
