package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.repository.Criteria;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DepartmentConfigurationOverride;
import com.warehouse.organisationstructure.operatorconfiguration.domain.port.secondary.DepartmentConfigurationOverrideRepository;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.DepartmentConfigurationOverrideEntity;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.mapper.DepartmentConfigurationOverrideMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public class DepartmentConfigurationOverrideRepositoryImpl implements DepartmentConfigurationOverrideRepository {

    private final OperatorFilteredRepository<DepartmentConfigurationOverrideEntity> repository;

    private final DepartmentConfigurationOverrideMapper mapper;

    public DepartmentConfigurationOverrideRepositoryImpl(
            final OperatorFilteredRepository<DepartmentConfigurationOverrideEntity> repository,
            final DepartmentConfigurationOverrideMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DepartmentConfigurationOverride> findByDepartmentId(final DepartmentId departmentId) {
        return byDepartmentId(departmentId).one().map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentConfigurationOverride> findAll() {
        return repository.createCriteria(DepartmentConfigurationOverrideEntity.class)
                .list()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public void save(final DepartmentConfigurationOverride override) {
        final DepartmentConfigurationOverrideEntity entity = mapper.toEntity(override);
        if (byDepartmentId(override.departmentId()).one().isPresent()) {
            repository.update(entity);
        } else {
            repository.create(entity);
        }
    }

    @Override
    @Transactional
    public void delete(final DepartmentId departmentId) {
        byDepartmentId(departmentId).one().ifPresent(repository::delete);
    }

    private Criteria<DepartmentConfigurationOverrideEntity> byDepartmentId(final DepartmentId departmentId) {
        return repository.createCriteria(DepartmentConfigurationOverrideEntity.class)
                .eq("departmentId.value", departmentId.getValue());
    }
}
