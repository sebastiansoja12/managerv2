package com.warehouse.supplierconfiguration.infrastructure.adapter.secondary;

import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.supplierconfiguration.application.port.secondary.SupplierConfigurationPolicyRepository;
import com.warehouse.supplierconfiguration.domain.model.SupplierConfigurationPolicy;
import com.warehouse.supplierconfiguration.infrastructure.adapter.secondary.entity.SupplierConfigurationPolicyEntity;
import com.warehouse.supplierconfiguration.infrastructure.adapter.secondary.mapper.SupplierConfigurationPersistenceMapper;
import jakarta.persistence.EntityManager;

import java.util.Optional;

public class SupplierConfigurationPolicyRepositoryImpl implements SupplierConfigurationPolicyRepository {

    private final OperatorFilteredRepository<SupplierConfigurationPolicyEntity> repository;
    private final SupplierConfigurationPersistenceMapper persistenceMapper;
    private final EntityManager entityManager;

    public SupplierConfigurationPolicyRepositoryImpl(
            final OperatorFilteredRepository<SupplierConfigurationPolicyEntity> repository,
            final SupplierConfigurationPersistenceMapper persistenceMapper,
            final EntityManager entityManager) {
        this.repository = repository;
        this.persistenceMapper = persistenceMapper;
        this.entityManager = entityManager;
    }

    @Override
    public Optional<SupplierConfigurationPolicy> findCurrent() {
        return findEntity().map(this.persistenceMapper::toModel);
    }

    @Override
    public SupplierConfigurationPolicy save(final SupplierConfigurationPolicy policy) {
        final SupplierConfigurationPolicyEntity entity = this.persistenceMapper.toEntity(policy);
        if (policy.getVersion() == null) {
            this.repository.create(entity);
        } else {
            this.repository.update(entity);
        }
        this.entityManager.flush();
        return findEntity()
                .map(this.persistenceMapper::toModel)
                .orElseThrow();
    }

    private Optional<SupplierConfigurationPolicyEntity> findEntity() {
        return this.repository.createCriteria(SupplierConfigurationPolicyEntity.class).one();
    }
}
