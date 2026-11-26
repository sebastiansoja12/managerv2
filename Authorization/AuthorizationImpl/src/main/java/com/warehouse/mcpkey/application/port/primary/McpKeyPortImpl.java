package com.warehouse.mcpkey.application.port.primary;

import com.warehouse.commonassets.exception.ProblemDetailsException;
import com.warehouse.commonassets.identificator.McpKeyId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.mcpkey.application.port.primary.command.GenerateMcpKeyCommand;
import com.warehouse.mcpkey.application.port.primary.result.GeneratedMcpKey;
import com.warehouse.mcpkey.application.port.primary.result.McpKeyAuthentication;
import com.warehouse.mcpkey.application.port.secondary.CurrentUserServicePort;
import com.warehouse.mcpkey.application.port.secondary.McpKeyRepository;
import com.warehouse.mcpkey.application.port.secondary.UserMcpKeyServicePort;
import com.warehouse.mcpkey.domain.exception.McpKeyNotFoundException;
import com.warehouse.mcpkey.domain.model.McpAccessPolicy;
import com.warehouse.mcpkey.domain.model.McpKey;
import com.warehouse.mcpkey.domain.service.McpKeyFactory;
import com.warehouse.mcpkey.domain.vo.McpKeySecret;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
public class McpKeyPortImpl implements McpKeyPort {

    private final McpKeyRepository mcpKeyRepository;
    private final McpKeyFactory mcpKeyFactory;
    private final CurrentUserServicePort currentUserServicePort;
    private final UserMcpKeyServicePort userMcpKeyServicePort;
    private final Clock clock;
    private final McpAccessPort mcpAccessPort;

    public McpKeyPortImpl(final McpKeyRepository mcpKeyRepository,
                          final McpKeyFactory mcpKeyFactory,
                          final CurrentUserServicePort currentUserServicePort,
                          final UserMcpKeyServicePort userMcpKeyServicePort,
                          final Clock clock, final McpAccessPort mcpAccessPort) {
        this.mcpKeyRepository = mcpKeyRepository;
        this.mcpKeyFactory = mcpKeyFactory;
        this.currentUserServicePort = currentUserServicePort;
        this.userMcpKeyServicePort = userMcpKeyServicePort;
        this.clock = clock;
        this.mcpAccessPort = mcpAccessPort;
    }

    @Override
    @Transactional
    public GeneratedMcpKey generate(final GenerateMcpKeyCommand command) {
        final UserId ownerId = this.currentUserServicePort.currentUserId();
        if (!this.mcpAccessPort.findForUser(ownerId).enabled()) {
            throw new ProblemDetailsException(403, "MCP access is disabled for this user");
        }
        final McpKeySecret secret = McpKeySecret.generate();
        final McpKey mcpKey = this.mcpKeyFactory.create(ownerId, command.name(), secret, command.expiresAt());
        final Instant timestamp = mcpKey.getCreatedAt();
        this.mcpKeyRepository.findByOwnerId(ownerId).forEach(previousKey -> {
            previousKey.revoke(timestamp);
            this.mcpKeyRepository.save(previousKey);
        });
        this.mcpKeyRepository.save(mcpKey);
        this.mcpKeyRepository.saveSecret(mcpKey.getMcpKeyId(), secret);
        this.userMcpKeyServicePort.assignMcpKey(ownerId, mcpKey.getMcpKeyId());
        log.info("MCP key {} generated for user {}", mcpKey.getMcpKeyId().value(), ownerId.value());
        return new GeneratedMcpKey(mcpKey, secret);
    }

    @Override
    public McpAccessPolicy findCurrentUserAccess() {
        return this.mcpAccessPort.findForUser(this.currentUserServicePort.currentUserId());
    }

    @Override
    public List<McpKey> findCurrentUserKeys() {
        return this.mcpKeyRepository.findByOwnerId(this.currentUserServicePort.currentUserId());
    }

    @Override
    @Transactional(readOnly = true)
    public McpKeySecret reveal(final McpKeyId mcpKeyId) {
        final UserId ownerId = this.currentUserServicePort.currentUserId();
        if (!this.mcpAccessPort.findForUser(ownerId).enabled()) {
            throw new ProblemDetailsException(403, "MCP access is disabled for this user");
        }
        this.mcpKeyRepository.findById(mcpKeyId)
                .filter(key -> key.isOwnedBy(ownerId) && key.isActiveAt(this.clock.instant()))
                .orElseThrow(() -> new McpKeyNotFoundException("Active MCP key was not found"));
        return this.mcpKeyRepository.findSecretById(mcpKeyId)
                .orElseThrow(() -> new ProblemDetailsException(409,
                        "This MCP key was stored as a hash only. Generate a new key to enable revealing it."));
    }

    @Override
    @Transactional
    public void revoke(final McpKeyId mcpKeyId) {
        final UserId ownerId = this.currentUserServicePort.currentUserId();
        final McpKey mcpKey = this.mcpKeyRepository.findById(mcpKeyId)
                .filter(key -> key.isOwnedBy(ownerId))
                .orElseThrow(() -> new McpKeyNotFoundException("MCP key was not found"));
        mcpKey.revoke(this.clock.instant());
        this.mcpKeyRepository.save(mcpKey);
        this.userMcpKeyServicePort.removeMcpKey(ownerId, mcpKeyId);
        log.info("MCP key {} revoked by user {}", mcpKeyId.value(), ownerId.value());
    }

    @Override
    @Transactional
    public Optional<McpKeyAuthentication> authenticate(final McpKeySecret secret) {
        final Instant timestamp = this.clock.instant();
        final Optional<McpKey> activeKey = this.mcpKeyRepository.findByKeyHash(secret.hash())
                .filter(key -> key.isActiveAt(timestamp));
        activeKey.ifPresent(key -> {
            key.recordUsage(timestamp);
            this.mcpKeyRepository.save(key);
        });
        return activeKey.map(key -> new McpKeyAuthentication(key.getMcpKeyId(), key.getOwnerId(),
                key.getOperatorId(), key.getCreatedAt()));
    }
}
