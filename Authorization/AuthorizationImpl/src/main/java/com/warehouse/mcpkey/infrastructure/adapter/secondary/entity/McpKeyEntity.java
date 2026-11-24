package com.warehouse.mcpkey.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.McpKeyId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.BelongsToOperator;
import com.warehouse.mcpkey.domain.enumeration.McpKeyStatus;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity(name = "mcpkey.McpKeyEntity")
@Table(name = "mcp_keys")
public class McpKeyEntity extends BelongsToOperator {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "mcp_key_id", nullable = false))
    private McpKeyId mcpKeyId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "owner_id", nullable = false))
    private UserId ownerId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "key_hash", nullable = false, length = 64)
    private String keyHash;

    @Column(name = "encrypted_secret")
    private String encryptedSecret;

    @Column(name = "key_prefix", nullable = false, length = 16)
    private String keyPrefix;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private McpKeyStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Column(name = "last_used_at")
    private Instant lastUsedAt;

    @Column(name = "revoked_at")
    private Instant revokedAt;

    protected McpKeyEntity() {
    }

    public McpKeyEntity(final McpKeyId mcpKeyId,
                        final UserId ownerId,
                        final String name,
                        final String keyHash,
                        final String keyPrefix,
                        final McpKeyStatus status,
                        final Instant createdAt,
                        final Instant expiresAt,
                        final Instant lastUsedAt,
                        final Instant revokedAt) {
        this.mcpKeyId = mcpKeyId;
        this.ownerId = ownerId;
        this.name = name;
        this.keyHash = keyHash;
        this.keyPrefix = keyPrefix;
        this.status = status;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.lastUsedAt = lastUsedAt;
        this.revokedAt = revokedAt;
    }

    public void updateState(final McpKeyStatus status, final Instant lastUsedAt, final Instant revokedAt) {
        this.status = status;
        this.lastUsedAt = lastUsedAt;
        this.revokedAt = revokedAt;
        if (status == McpKeyStatus.REVOKED) {
            this.encryptedSecret = null;
        }
    }

    public String getEncryptedSecret() {
        return encryptedSecret;
    }

    public void setEncryptedSecret(final String encryptedSecret) {
        this.encryptedSecret = encryptedSecret;
    }

    public McpKeyId getMcpKeyId() {
        return mcpKeyId;
    }

    public UserId getOwnerId() {
        return ownerId;
    }

    public String getName() {
        return name;
    }

    public String getKeyHash() {
        return keyHash;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public McpKeyStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public Instant getLastUsedAt() {
        return lastUsedAt;
    }

    public Instant getRevokedAt() {
        return revokedAt;
    }
}
