package com.warehouse.mcpkey.infrastructure.adapter.primary.dto;

import com.warehouse.auth.McpModule;
import com.warehouse.mcpkey.domain.enumeration.McpModuleAccess;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record McpAccessApiRequest(@NotNull Boolean enabled,
                                  @NotNull Map<@NotNull McpModule, @NotNull McpModuleAccess> modules) {
}
