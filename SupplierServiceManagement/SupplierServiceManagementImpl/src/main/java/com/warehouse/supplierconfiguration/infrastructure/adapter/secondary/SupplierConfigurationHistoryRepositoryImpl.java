package com.warehouse.supplierconfiguration.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.supplierconfiguration.application.port.secondary.SupplierConfigurationHistoryRepository;
import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationRevision;
import com.warehouse.supplierconfiguration.infrastructure.adapter.secondary.entity.SupplierConfigurationChangeEntity;
import com.warehouse.supplierconfiguration.infrastructure.adapter.secondary.mapper.SupplierConfigurationPersistenceMapper;

import java.util.List;

public class SupplierConfigurationHistoryRepositoryImpl implements SupplierConfigurationHistoryRepository {

    private final OperatorFilteredRepository<SupplierConfigurationChangeEntity> repository;
    private final SupplierConfigurationPersistenceMapper persistenceMapper;

    public SupplierConfigurationHistoryRepositoryImpl(
            final OperatorFilteredRepository<SupplierConfigurationChangeEntity> repository,
            final SupplierConfigurationPersistenceMapper persistenceMapper) {
        this.repository = repository;
        this.persistenceMapper = persistenceMapper;
    }

    @Override
    public void append(final SupplierConfigurationRevision revision) {
        this.persistenceMapper.toEntities(revision).forEach(this.repository::create);
    }

    @Override
    public List<SupplierConfigurationRevision> findBySupplierId(final SupplierId supplierId) {
        return this.persistenceMapper.toRevisions(this.repository
                .createCriteria(SupplierConfigurationChangeEntity.class)
                .eq("supplierId.value", supplierId.value())
                .list());
    }
}
