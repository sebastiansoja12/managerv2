package com.warehouse.mcpkey.domain.model;

import com.warehouse.commonassets.identificator.McpKeyId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.mcpkey.domain.enumeration.McpKeyStatus;

import java.time.Duration;
import java.time.Instant;

public class McpKey {

    private static final Duration USAGE_RECORDING_INTERVAL = Duration.ofMinutes(1);

    private final McpKeyId mcpKeyId;
    private final UserId ownerId;
    private final String name;
    private final String keyHash;
    private final String keyPrefix;
    private final Instant createdAt;
    private final Instant expiresAt;
    private Instant lastUsedAt;
    private Instant revokedAt;
    private McpKeyStatus status;
    private final OperatorId operatorId;

    public McpKey(final McpKeyId mcpKeyId,
                  final UserId ownerId,
                  final String name,
                  final String keyHash,
                  final String keyPrefix,
                  final Instant createdAt,
                  final Instant expiresAt,
                  final Instant lastUsedAt,
                  final Instant revokedAt,
                  final McpKeyStatus status,
                  final OperatorId operatorId) {
        this.mcpKeyId = mcpKeyId;
        this.ownerId = ownerId;
        this.name = name;
        this.keyHash = keyHash;
        this.keyPrefix = keyPrefix;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.lastUsedAt = lastUsedAt;
        this.revokedAt = revokedAt;
        this.status = status;
        this.operatorId = operatorId;
    }

    public void revoke(final Instant timestamp) {
        if (McpKeyStatus.REVOKED.equals(this.status)) {
            return;
        }
        this.status = McpKeyStatus.REVOKED;
        this.revokedAt = timestamp;
    }

    public void recordUsage(final Instant timestamp) {
        if (this.lastUsedAt != null && timestamp.isBefore(this.lastUsedAt.plus(USAGE_RECORDING_INTERVAL))) {
            return;
        }
        this.lastUsedAt = timestamp;
    }

    public boolean isActiveAt(final Instant timestamp) {
        return McpKeyStatus.ACTIVE.equals(this.status) && (this.expiresAt == null || timestamp.isBefore(this.expiresAt));
    }

    public boolean isOwnedBy(final UserId userId) {
        return this.ownerId.equals(userId);
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

    public McpKeyStatus getStatus() {
        return status;
    }

    public OperatorId getOperatorId() {
        return operatorId;
    }
}
