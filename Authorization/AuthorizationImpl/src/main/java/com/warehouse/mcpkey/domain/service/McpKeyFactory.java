package com.warehouse.mcpkey.domain.service;

import com.warehouse.commonassets.identificator.McpKeyId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.mcpkey.domain.enumeration.McpKeyStatus;
import com.warehouse.mcpkey.domain.exception.InvalidMcpKeyException;
import com.warehouse.mcpkey.domain.model.McpKey;
import com.warehouse.mcpkey.domain.vo.McpKeySecret;

import java.time.Clock;
import java.time.Instant;

public class McpKeyFactory {

    public static final int NAME_MAX_LENGTH = 100;

    private final Clock clock;

    public McpKeyFactory(final Clock clock) {
        this.clock = clock;
    }

    public McpKey create(final UserId ownerId,
                         final String name,
                         final McpKeySecret secret,
                         final Instant expiresAt) {
        final Instant timestamp = this.clock.instant();
        if (name == null || name.isBlank()) {
            throw new InvalidMcpKeyException("MCP key name is required");
        }
        final String normalizedName = name.trim();
        if (normalizedName.length() > NAME_MAX_LENGTH) {
            throw new InvalidMcpKeyException("MCP key name must not exceed " + NAME_MAX_LENGTH + " characters");
        }
        if (expiresAt != null && !expiresAt.isAfter(timestamp)) {
            throw new InvalidMcpKeyException("MCP key expiration date must be in the future");
        }
        return new McpKey(McpKeyId.generate(), ownerId, normalizedName, secret.hash(), secret.visiblePrefix(),
                timestamp, expiresAt, null, null, McpKeyStatus.ACTIVE, null);
    }
}
