package com.warehouse.mcpkey.infrastructure.adapter.primary.dto;

import com.warehouse.auth.McpModule;
import com.warehouse.mcpkey.domain.enumeration.McpModuleAccess;

import java.util.Map;

public record McpAccessApiResponse(boolean enabled, Map<McpModule, McpModuleAccess> modules) {
}
