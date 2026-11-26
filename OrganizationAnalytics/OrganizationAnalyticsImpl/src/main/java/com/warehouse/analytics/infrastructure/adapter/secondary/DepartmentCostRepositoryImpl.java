package com.warehouse.analytics.infrastructure.adapter.secondary;

import com.warehouse.analytics.application.port.secondary.DepartmentCostRepository;
import com.warehouse.analytics.domain.model.DepartmentCost;
import com.warehouse.analytics.domain.vo.AnalyticsPeriod;
import com.warehouse.analytics.infrastructure.adapter.secondary.entity.DepartmentCostEntity;
import com.warehouse.analytics.infrastructure.adapter.secondary.mapper.DepartmentCostPersistenceMapper;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentCostId;
import com.warehouse.commonassets.repository.Criteria;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;

import java.util.List;
import java.util.Optional;

public class DepartmentCostRepositoryImpl implements DepartmentCostRepository {

    private final OperatorFilteredRepository<DepartmentCostEntity> repository;

    private final DepartmentCostPersistenceMapper persistenceMapper;

    public DepartmentCostRepositoryImpl(final OperatorFilteredRepository<DepartmentCostEntity> repository,
                                        final DepartmentCostPersistenceMapper persistenceMapper) {
        this.repository = repository;
        this.persistenceMapper = persistenceMapper;
    }

    @Override
    public void create(final DepartmentCost departmentCost) {
        this.repository.create(this.persistenceMapper.toEntity(departmentCost));
    }

    @Override
    public Optional<DepartmentCost> findById(final DepartmentCostId costId) {
        return entity(costId).map(this.persistenceMapper::toDomain);
    }

    @Override
    public void delete(final DepartmentCostId costId) {
        entity(costId).ifPresent(this.repository::delete);
    }

    @Override
    public List<DepartmentCost> find(final AnalyticsPeriod period, final Optional<DepartmentCode> departmentCode) {
        final Criteria<DepartmentCostEntity> criteria = this.repository.createCriteria(DepartmentCostEntity.class)
                .ge("incurredOn", period.dateFrom())
                .le("incurredOn", period.dateTo());
        departmentCode.ifPresent(code -> criteria.eq("departmentCode.value", code.value()));
        return criteria.list().stream()
                .map(this.persistenceMapper::toDomain)
                .toList();
    }

    private Optional<DepartmentCostEntity> entity(final DepartmentCostId costId) {
        return this.repository.createCriteria(DepartmentCostEntity.class)
                .eq("costId.value", costId.value())
                .one();
    }
}
