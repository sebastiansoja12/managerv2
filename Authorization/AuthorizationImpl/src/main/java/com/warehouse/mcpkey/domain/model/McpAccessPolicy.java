package com.warehouse.mcpkey.domain.model;

import com.warehouse.auth.McpModule;
import com.warehouse.mcpkey.domain.enumeration.McpModuleAccess;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public record McpAccessPolicy(boolean enabled, Map<McpModule, McpModuleAccess> modules) {

    public McpAccessPolicy {
        modules = Map.copyOf(modules);
    }

    public static McpAccessPolicy unrestricted() {
        return new McpAccessPolicy(true, Arrays.stream(McpModule.values())
                .collect(Collectors.toMap(module -> module, module -> McpModuleAccess.WRITE)));
    }

    public boolean allows(final McpModule module, final boolean write) {
        return enabled && modules.getOrDefault(module, McpModuleAccess.NONE).allows(write);
    }
}
