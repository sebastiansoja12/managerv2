package com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.repository.Criteria;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.organisationstructure.deliveryarea.application.port.secondary.DeliveryAreaRepository;
import com.warehouse.organisationstructure.deliveryarea.domain.model.DeliveryArea;
import com.warehouse.commonassets.identificator.DeliveryAreaId;
import com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.secondary.entity.DeliveryAreaEntity;
import com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.secondary.mapper.DeliveryAreaPersistenceMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public class DeliveryAreaRepositoryAdapter implements DeliveryAreaRepository {

    private final OperatorFilteredRepository<DeliveryAreaEntity> repository;

    private final DeliveryAreaPersistenceMapper persistenceMapper;

    public DeliveryAreaRepositoryAdapter(final OperatorFilteredRepository<DeliveryAreaEntity> repository,
                                         final DeliveryAreaPersistenceMapper persistenceMapper) {
        this.repository = repository;
        this.persistenceMapper = persistenceMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryArea> findAll() {
        return repository.createCriteria(DeliveryAreaEntity.class)
                .asc("createdAt")
                .list()
                .stream()
                .map(persistenceMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryArea> findByDepartmentId(final DepartmentId departmentId) {
        return repository.createCriteria(DeliveryAreaEntity.class)
                .eq("departmentId.value", departmentId.getValue())
                .asc("createdAt")
                .list()
                .stream()
                .map(persistenceMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DeliveryArea> findById(final DeliveryAreaId deliveryAreaId) {
        return byId(deliveryAreaId).one().map(persistenceMapper::toDomain);
    }

    @Override
    @Transactional
    public DeliveryArea create(final DeliveryArea deliveryArea) {
        repository.create(persistenceMapper.toEntity(deliveryArea));
        return deliveryArea;
    }

    @Override
    @Transactional
    public DeliveryArea update(final DeliveryArea deliveryArea) {
        repository.update(persistenceMapper.toEntity(deliveryArea));
        return deliveryArea;
    }

    @Override
    @Transactional
    public void delete(final DeliveryAreaId deliveryAreaId) {
        byId(deliveryAreaId).one().ifPresent(repository::delete);
    }

    private Criteria<DeliveryAreaEntity> byId(final DeliveryAreaId deliveryAreaId) {
        return repository.createCriteria(DeliveryAreaEntity.class)
                .eq("deliveryAreaId.value", deliveryAreaId.value());
    }
}
