package com.warehouse.auth.configuration.access;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.warehouse.auth.Mcp;
import com.warehouse.auth.McpModule;
import com.warehouse.auth.configuration.McpKeyAuthenticationFilter;
import com.warehouse.auth.domain.model.OrganizationUser;
import com.warehouse.auth.domain.service.UserService;
import com.warehouse.commonassets.enumeration.AccountLevel;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.UsernameTenantPasswordAuthenticationToken;
import com.warehouse.mcpkey.application.port.primary.McpAccessPort;
import com.warehouse.mcpkey.domain.enumeration.McpModuleAccess;
import com.warehouse.mcpkey.domain.model.McpAccessPolicy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Map;

class McpAccessAspectTest {

    private static final UserId USER_ID = new UserId(5L);

    private final McpAccessPort accessPort = mock(McpAccessPort.class);
    private final UserService userService = mock(UserService.class);
    private final McpTools tools = proxy(new McpTools());

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldAllowToolForUserAuthenticatedWithMcpKey() {
        authenticate(OrganizationUser.Role.USER, true);

        assertThat(tools.read()).isEqualTo("read");
    }

    @Test
    void shouldRejectToolWithoutMcpKeyAuthentication() {
        authenticate(OrganizationUser.Role.SUPERADMIN, false);

        assertThatThrownBy(tools::read)
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("MCP key authentication is required");
    }

    @Test
    void shouldRejectToolRequiringHigherRole() {
        authenticate(OrganizationUser.Role.USER, true);

        assertThatThrownBy(tools::change)
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("This tool requires the MANAGER role or higher");
    }

    @Test
    void shouldRejectManagerForToolRequiringAdministrator() {
        authenticate(OrganizationUser.Role.MANAGER, true);

        assertThat(tools.change()).isEqualTo("change");
        assertThatThrownBy(tools::changeStatus)
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("This tool requires the ADMIN role or higher");
    }

    @Test
    void shouldAllowAdministratorToChangeStatus() {
        authenticate(OrganizationUser.Role.ADMIN, true);

        assertThat(tools.changeStatus()).isEqualTo("changeStatus");
    }

    @Test
    void shouldRejectDeletedUser() {
        final OrganizationUser user = authenticate(OrganizationUser.Role.SUPERADMIN, true);
        user.setDeleted(true);

        assertThatThrownBy(tools::read)
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("User is inactive or deleted");
    }

    @Test
    void shouldEnforceReadOnlyModuleEvenForAdministrator() {
        authenticate(OrganizationUser.Role.ADMIN, true);
        when(accessPort.findForUser(USER_ID)).thenReturn(new McpAccessPolicy(true,
                Map.of(McpModule.SHIPMENTS,
                        McpModuleAccess.READ)));

        assertThat(tools.read()).isEqualTo("read");
        assertThatThrownBy(tools::change).isInstanceOf(AccessDeniedException.class);
        assertThatThrownBy(tools::changeStatus).isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void shouldRejectAllToolsWhenMcpIsBlocked() {
        authenticate(OrganizationUser.Role.SUPERADMIN, true);
        when(accessPort.findForUser(USER_ID)).thenReturn(new McpAccessPolicy(false, Map.of()));

        assertThatThrownBy(tools::read).isInstanceOf(AccessDeniedException.class);
    }

    private OrganizationUser authenticate(final OrganizationUser.Role role, final boolean mcpKey) {
        final OrganizationUser user = OrganizationUser.of(USER_ID, "user", "password", "Jan", "Nowak",
                "user@test.pl", role, new DepartmentId(10L), null);
        when(userService.findUserById(USER_ID)).thenReturn(user);
        when(accessPort.findForUser(USER_ID)).thenReturn(McpAccessPolicy.unrestricted());
        SecurityContextHolder.getContext().setAuthentication(new UsernameTenantPasswordAuthenticationToken(
                USER_ID, OperatorId.of(20L), new DepartmentId(10L), null,
                mcpKey ? List.of(new SimpleGrantedAuthority(McpKeyAuthenticationFilter.MCP_AUTHORITY)) : List.of()));
        return user;
    }

    private McpTools proxy(final McpTools target) {
        final AspectJProxyFactory proxyFactory = new AspectJProxyFactory(target);
        proxyFactory.setProxyTargetClass(true);
        proxyFactory.addAspect(new McpAccessAspect(new McpAccessEvaluator(userService, accessPort)));
        return proxyFactory.getProxy();
    }

    static class McpTools {

        @Mcp(module = McpModule.SHIPMENTS)
        public String read() {
            return "read";
        }

        @Mcp(module = McpModule.SHIPMENTS, write = true, minimumLevel = AccountLevel.MANAGER)
        public String change() {
            return "change";
        }

        @Mcp(module = McpModule.DEPARTMENTS, write = true, minimumLevel = AccountLevel.ADMIN)
        public String changeStatus() {
            return "changeStatus";
        }
    }
}
