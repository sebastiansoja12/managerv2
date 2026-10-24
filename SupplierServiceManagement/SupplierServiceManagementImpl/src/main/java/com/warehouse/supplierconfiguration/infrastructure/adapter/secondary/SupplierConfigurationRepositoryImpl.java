package com.warehouse.supplierconfiguration.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.supplierconfiguration.application.port.secondary.SupplierConfigurationRepository;
import com.warehouse.supplierconfiguration.domain.model.SupplierConfiguration;
import com.warehouse.supplierconfiguration.infrastructure.adapter.secondary.entity.SupplierConfigurationEntity;
import com.warehouse.supplierconfiguration.infrastructure.adapter.secondary.mapper.SupplierConfigurationPersistenceMapper;
import jakarta.persistence.EntityManager;

import java.util.Optional;

public class SupplierConfigurationRepositoryImpl implements SupplierConfigurationRepository {

    private final OperatorFilteredRepository<SupplierConfigurationEntity> repository;
    private final SupplierConfigurationPersistenceMapper persistenceMapper;
    private final EntityManager entityManager;

    public SupplierConfigurationRepositoryImpl(
            final OperatorFilteredRepository<SupplierConfigurationEntity> repository,
            final SupplierConfigurationPersistenceMapper persistenceMapper,
            final EntityManager entityManager) {
        this.repository = repository;
        this.persistenceMapper = persistenceMapper;
        this.entityManager = entityManager;
    }

    @Override
    public Optional<SupplierConfiguration> findBySupplierId(final SupplierId supplierId) {
        return findEntity(supplierId).map(this.persistenceMapper::toModel);
    }

    @Override
    public SupplierConfiguration save(final SupplierConfiguration supplierConfiguration) {
        final SupplierConfigurationEntity entity = this.persistenceMapper.toEntity(supplierConfiguration);
        if (supplierConfiguration.getVersion() == null) {
            this.repository.create(entity);
        } else {
            this.repository.update(entity);
        }
        this.entityManager.flush();
        return findEntity(supplierConfiguration.getSupplierId())
                .map(this.persistenceMapper::toModel)
                .orElseThrow();
    }

    private Optional<SupplierConfigurationEntity> findEntity(final SupplierId supplierId) {
        return this.repository.createCriteria(SupplierConfigurationEntity.class)
                .eq("supplierId.value", supplierId.value())
                .one();
    }
}
