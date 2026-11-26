package com.warehouse.mcpkey.infrastructure.adapter.primary.dto;

import java.time.Instant;

public record McpKeyApiResponse(McpKeyIdDto mcpKeyId,
                                String name,
                                String keyPrefix,
                                String status,
                                Instant createdAt,
                                Instant expiresAt,
                                Instant lastUsedAt,
                                Instant revokedAt) {
}
