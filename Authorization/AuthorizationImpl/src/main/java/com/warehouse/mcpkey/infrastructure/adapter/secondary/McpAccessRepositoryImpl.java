package com.warehouse.mcpkey.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.mcpkey.application.port.secondary.McpAccessRepository;
import com.warehouse.mcpkey.domain.model.McpAccessPolicy;
import com.warehouse.mcpkey.infrastructure.adapter.secondary.entity.McpAccessEntity;

import java.util.Optional;

public class McpAccessRepositoryImpl implements McpAccessRepository {

    private final OperatorFilteredRepository<McpAccessEntity> repository;

    public McpAccessRepositoryImpl(final OperatorFilteredRepository<McpAccessEntity> repository) {
        this.repository = repository;
    }

    @Override
    public Optional<McpAccessPolicy> findByUserId(final UserId userId) {
        return this.repository.createCriteria(McpAccessEntity.class)
                .eq("userId.value", userId.value()).one()
                .map(entity -> new McpAccessPolicy(entity.isEnabled(), entity.getModules()));
    }

    @Override
    public void save(final UserId userId, final McpAccessPolicy policy) {
        this.repository.update(new McpAccessEntity(userId, policy.enabled(), policy.modules()));
    }
}
