package com.warehouse.mcpkey.infrastructure.adapter.primary.dto;

import java.time.Instant;

public record GenerateMcpKeyApiRequest(String name, Instant expiresAt) {
}
