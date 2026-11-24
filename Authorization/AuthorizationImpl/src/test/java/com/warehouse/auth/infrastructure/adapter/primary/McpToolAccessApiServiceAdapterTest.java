package com.warehouse.auth.infrastructure.adapter.primary;

import com.warehouse.auth.Mcp;
import com.warehouse.auth.McpModule;
import com.warehouse.auth.domain.model.OrganizationUser;
import com.warehouse.auth.domain.port.primary.CurrentUserAuthenticationPort;
import com.warehouse.commonassets.enumeration.AccountLevel;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.mcpkey.McpAccessFixture;
import com.warehouse.mcpkey.McpKeyFixture;
import com.warehouse.mcpkey.application.port.primary.McpAccessPort;
import com.warehouse.mcpkey.domain.enumeration.McpModuleAccess;
import com.warehouse.mcpkey.domain.model.McpAccessPolicy;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class McpToolAccessApiServiceAdapterTest {

    private final CurrentUserAuthenticationPort currentUser = mock(CurrentUserAuthenticationPort.class);
    private final McpAccessPort access = mock(McpAccessPort.class);
    private final McpToolAccessApiServiceAdapter adapter = new McpToolAccessApiServiceAdapter(currentUser, access);

    @Test
    void shouldExposeOnlyToolsAllowedByRoleAndModulePolicy() throws Exception {
        when(currentUser.getCurrentUser()).thenReturn(McpAccessFixture.user(McpKeyFixture.OWNER_ID,
                OrganizationUser.Role.USER, McpKeyFixture.OPERATOR_ID, new DepartmentId(1L)));
        when(access.findForUser(McpKeyFixture.OWNER_ID)).thenReturn(McpAccessPolicy.unrestricted());

        assertThat(adapter.allowedTools(rules())).containsExactly("read");

        when(currentUser.getCurrentUser()).thenReturn(McpAccessFixture.user(McpKeyFixture.OWNER_ID,
                OrganizationUser.Role.ADMIN, McpKeyFixture.OPERATOR_ID, new DepartmentId(1L)));
        when(access.findForUser(McpKeyFixture.OWNER_ID)).thenReturn(new McpAccessPolicy(true,
                Map.of(McpModule.SHIPMENTS, McpModuleAccess.READ)));

        assertThat(adapter.allowedTools(rules())).containsExactly("read");
    }

    @Test
    void shouldExposeNoToolsWhenMcpIsDisabled() throws Exception {
        when(currentUser.getCurrentUser()).thenReturn(McpAccessFixture.user(McpKeyFixture.OWNER_ID,
                OrganizationUser.Role.SUPERADMIN, McpKeyFixture.OPERATOR_ID, new DepartmentId(1L)));
        when(access.findForUser(McpKeyFixture.OWNER_ID)).thenReturn(new McpAccessPolicy(false,
                McpAccessPolicy.unrestricted().modules()));

        assertThat(adapter.allowedTools(rules())).isEmpty();
    }

    private Map<String, Mcp> rules() throws Exception {
        return Map.of("read", Tools.class.getMethod("read").getAnnotation(Mcp.class),
                "write", Tools.class.getMethod("write").getAnnotation(Mcp.class));
    }

    static class Tools {
        @Mcp(module = McpModule.SHIPMENTS)
        public void read() {
        }

        @Mcp(module = McpModule.SHIPMENTS, write = true, minimumLevel = AccountLevel.MANAGER)
        public void write() {
        }
    }
}
