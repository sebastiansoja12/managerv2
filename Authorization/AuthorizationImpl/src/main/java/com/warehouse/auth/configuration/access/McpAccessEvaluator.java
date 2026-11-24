package com.warehouse.auth.configuration.access;

import com.warehouse.auth.Mcp;
import com.warehouse.auth.McpModule;
import com.warehouse.auth.configuration.McpKeyAuthenticationFilter;
import com.warehouse.auth.domain.model.OrganizationUser;
import com.warehouse.auth.domain.service.UserService;
import com.warehouse.commonassets.enumeration.AccountLevel;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.mcpkey.application.port.primary.McpAccessPort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class McpAccessEvaluator {

    private final UserService userService;

    private final McpAccessPort accessPort;

    public McpAccessEvaluator(final UserService userService, final McpAccessPort accessPort) {
        this.userService = userService;
        this.accessPort = accessPort;
    }

    public void validate(final Authentication authentication, final Mcp mcp) {
        validate(authentication, mcp.module(), mcp.write(), mcp.minimumLevel());
    }

    private void validate(final Authentication authentication, final McpModule module,
                           final boolean write, final AccountLevel minimumLevel) {
        if (authentication == null || authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .noneMatch(McpKeyAuthenticationFilter.MCP_AUTHORITY::equals)) {
            throw new AccessDeniedException("MCP key authentication is required");
        }
        if (!(authentication.getPrincipal() instanceof final UserId userId)) {
            throw new AccessDeniedException("Authenticated user identifier is missing");
        }
        final OrganizationUser user = this.userService.findUserById(userId);
        if (user == null || Boolean.TRUE.equals(user.isDeleted())) {
            throw new AccessDeniedException("User is inactive or deleted");
        }
        if (!user.getLevel().isAtLeast(minimumLevel)) {
            throw new AccessDeniedException("This tool requires the " + minimumLevel + " role or higher");
        }
        if (!this.accessPort.findForUser(userId).allows(module, write)) {
            throw new AccessDeniedException("MCP access to this module is disabled for this user");
        }
    }
}
