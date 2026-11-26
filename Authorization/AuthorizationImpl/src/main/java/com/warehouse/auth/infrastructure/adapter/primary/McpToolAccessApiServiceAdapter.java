package com.warehouse.auth.infrastructure.adapter.primary;

import com.warehouse.auth.Mcp;
import com.warehouse.auth.McpToolAccessApiService;
import com.warehouse.auth.domain.model.OrganizationUser;
import com.warehouse.auth.domain.port.primary.CurrentUserAuthenticationPort;
import com.warehouse.mcpkey.application.port.primary.McpAccessPort;
import com.warehouse.mcpkey.domain.model.McpAccessPolicy;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class McpToolAccessApiServiceAdapter implements McpToolAccessApiService {

    private final CurrentUserAuthenticationPort currentUserPort;
    private final McpAccessPort accessPort;

    public McpToolAccessApiServiceAdapter(final CurrentUserAuthenticationPort currentUserPort,
                                           final McpAccessPort accessPort) {
        this.currentUserPort = currentUserPort;
        this.accessPort = accessPort;
    }

    @Override
    public Set<String> allowedTools(final Map<String, Mcp> rules) {
        final OrganizationUser user = this.currentUserPort.getCurrentUser();
        if (user == null || Boolean.TRUE.equals(user.isDeleted())) {
            return Set.of();
        }
        final McpAccessPolicy policy = this.accessPort.findForUser(user.getUserId());
        return rules.entrySet().stream()
                .filter(entry -> user.getLevel().isAtLeast(entry.getValue().minimumLevel()))
                .filter(entry -> policy.allows(entry.getValue().module(), entry.getValue().write()))
                .map(Map.Entry::getKey).collect(Collectors.toSet());
    }
}
