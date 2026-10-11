package com.warehouse.mcp.infrastructure.adapter.primary.response;

import java.util.Map;

public record ConfigurationToolResponse(String section, Map<String, Object> configuration) {
}
