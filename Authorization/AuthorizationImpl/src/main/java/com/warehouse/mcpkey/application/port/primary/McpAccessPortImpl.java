package com.warehouse.mcpkey.application.port.primary;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.mcpkey.application.port.secondary.McpAccessRepository;
import com.warehouse.mcpkey.application.port.secondary.McpKeyRepository;
import com.warehouse.mcpkey.application.port.secondary.UserMcpKeyServicePort;
import com.warehouse.mcpkey.domain.model.McpAccessPolicy;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;

public class McpAccessPortImpl implements McpAccessPort {

    private final McpAccessRepository accessRepository;
    private final McpKeyRepository keyRepository;
    private final UserMcpKeyServicePort userServicePort;
    private final Clock clock;

    public McpAccessPortImpl(final McpAccessRepository accessRepository, final McpKeyRepository keyRepository,
                             final UserMcpKeyServicePort userServicePort, final Clock clock) {
        this.accessRepository = accessRepository;
        this.keyRepository = keyRepository;
        this.userServicePort = userServicePort;
        this.clock = clock;
    }

    @Override
    public McpAccessPolicy findForUser(final UserId userId) {
        return this.accessRepository.findByUserId(userId).orElseGet(McpAccessPolicy::unrestricted);
    }

    @Override
    public McpAccessPolicy findManagedPolicy(final UserId userId) {
        this.userServicePort.requireManageableUser(userId);
        return findForUser(userId);
    }

    @Override
    @Transactional
    public void updateManagedPolicy(final UserId userId, final McpAccessPolicy policy) {
        this.userServicePort.requireManageableUser(userId);
        this.accessRepository.save(userId, policy);
        if (!policy.enabled()) {
            revokeKeys(userId);
        }
    }

    @Override
    @Transactional
    public void revokeManagedKeys(final UserId userId) {
        this.userServicePort.requireManageableUser(userId);
        revokeKeys(userId);
    }

    private void revokeKeys(final UserId userId) {
        final Instant timestamp = this.clock.instant();
        this.keyRepository.findByOwnerId(userId).forEach(key -> {
            key.revoke(timestamp);
            this.keyRepository.save(key);
            this.userServicePort.removeMcpKey(userId, key.getMcpKeyId());
        });
    }
}
