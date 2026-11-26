package com.warehouse.mcpkey.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.McpKeyId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.repository.NoOperatorContextRepository;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.mcpkey.application.port.secondary.McpKeyRepository;
import com.warehouse.mcpkey.domain.model.McpKey;
import com.warehouse.mcpkey.domain.vo.McpKeySecret;
import com.warehouse.commonassets.security.CredentialCipher;
import com.warehouse.mcpkey.infrastructure.adapter.secondary.entity.McpKeyEntity;

import java.util.List;
import java.util.Optional;

public class McpKeyRepositoryImpl implements McpKeyRepository {

    private final OperatorFilteredRepository<McpKeyEntity> repository;
    private final CredentialCipher credentialCipher;
    private final NoOperatorContextRepository<McpKeyEntity> noOperatorContextRepository;

    public McpKeyRepositoryImpl(final OperatorFilteredRepository<McpKeyEntity> repository,
                                final NoOperatorContextRepository<McpKeyEntity> noOperatorContextRepository,
                                final CredentialCipher credentialCipher) {
        this.repository = repository;
        this.credentialCipher = credentialCipher;
        this.noOperatorContextRepository = noOperatorContextRepository;
    }

    @Override
    public void save(final McpKey mcpKey) {
        if (mcpKey.getOperatorId() == null) {
            this.repository.create(toEntity(mcpKey));
        } else {
            final McpKeyEntity entity = this.noOperatorContextRepository.createCriteria(McpKeyEntity.class)
                    .eq("mcpKeyId.value", mcpKey.getMcpKeyId().value())
                    .eq("operatorId.value", mcpKey.getOperatorId().value())
                    .one().orElseThrow(() -> new IllegalStateException("Stored MCP key was not found"));
            entity.updateState(mcpKey.getStatus(), mcpKey.getLastUsedAt(), mcpKey.getRevokedAt());
            this.repository.update(entity);
        }
    }

    @Override
    public void saveSecret(final McpKeyId mcpKeyId, final McpKeySecret secret) {
        final McpKeyEntity entity = findEntityById(mcpKeyId)
                .orElseThrow(() -> new IllegalStateException("Stored MCP key was not found"));
        entity.setEncryptedSecret(this.credentialCipher.encrypt(secret.value()));
        this.repository.update(entity);
    }

    @Override
    public Optional<McpKeySecret> findSecretById(final McpKeyId mcpKeyId) {
        return findEntityById(mcpKeyId).map(McpKeyEntity::getEncryptedSecret)
                .map(this.credentialCipher::decrypt).map(McpKeySecret::new);
    }

    private Optional<McpKeyEntity> findEntityById(final McpKeyId mcpKeyId) {
        return this.repository.createCriteria(McpKeyEntity.class)
                .eq("mcpKeyId.value", mcpKeyId.value()).one();
    }

    @Override
    public Optional<McpKey> findById(final McpKeyId mcpKeyId) {
        return findEntityById(mcpKeyId).map(this::toModel);
    }

    @Override
    public List<McpKey> findByOwnerId(final UserId ownerId) {
        return this.repository.createCriteria(McpKeyEntity.class)
                .eq("ownerId.value", ownerId.value())
                .desc("createdAt")
                .list()
                .stream()
                .map(this::toModel)
                .toList();
    }

    @Override
    public Optional<McpKey> findByKeyHash(final String keyHash) {
        return this.noOperatorContextRepository.createCriteria(McpKeyEntity.class)
                .eq("keyHash", keyHash)
                .one()
                .map(this::toModel);
    }

    private McpKeyEntity toEntity(final McpKey mcpKey) {
        return new McpKeyEntity(
                mcpKey.getMcpKeyId(),
                mcpKey.getOwnerId(),
                mcpKey.getName(),
                mcpKey.getKeyHash(),
                mcpKey.getKeyPrefix(),
                mcpKey.getStatus(),
                mcpKey.getCreatedAt(),
                mcpKey.getExpiresAt(),
                mcpKey.getLastUsedAt(),
                mcpKey.getRevokedAt());
    }

    private McpKey toModel(final McpKeyEntity entity) {
        return new McpKey(
                entity.getMcpKeyId(),
                entity.getOwnerId(),
                entity.getName(),
                entity.getKeyHash(),
                entity.getKeyPrefix(),
                entity.getCreatedAt(),
                entity.getExpiresAt(),
                entity.getLastUsedAt(),
                entity.getRevokedAt(),
                entity.getStatus(),
                entity.operatorId());
    }
}
