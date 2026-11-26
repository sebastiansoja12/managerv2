package com.warehouse.mcpkey.domain.model;

import com.warehouse.auth.McpModule;
import com.warehouse.mcpkey.domain.enumeration.McpModuleAccess;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class McpAccessPolicyTest {

    @Test
    void shouldDenyMissingModulesAndWritesOnReadOnlyModules() {
        final McpAccessPolicy policy = new McpAccessPolicy(true, Map.of(McpModule.SHIPMENTS, McpModuleAccess.READ));

        assertThat(policy.allows(McpModule.SHIPMENTS, false)).isTrue();
        assertThat(policy.allows(McpModule.SHIPMENTS, true)).isFalse();
        assertThat(policy.allows(McpModule.CONFIGURATION, false)).isFalse();
    }

    @Test
    void shouldAllowReadsAndWritesWhenWritePermissionIsGranted() {
        final McpAccessPolicy policy = new McpAccessPolicy(true, Map.of(McpModule.DELIVERIES, McpModuleAccess.WRITE));

        assertThat(policy.allows(McpModule.DELIVERIES, false)).isTrue();
        assertThat(policy.allows(McpModule.DELIVERIES, true)).isTrue();
    }

    @Test
    void shouldBlockEveryModuleWhenDisabled() {
        final McpAccessPolicy policy = new McpAccessPolicy(false, McpAccessPolicy.unrestricted().modules());

        for (final McpModule module : McpModule.values()) {
            assertThat(policy.allows(module, false)).isFalse();
            assertThat(policy.allows(module, true)).isFalse();
        }
    }
}
