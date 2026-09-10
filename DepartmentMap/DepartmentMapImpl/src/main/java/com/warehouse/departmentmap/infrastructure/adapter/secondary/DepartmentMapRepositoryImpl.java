package com.warehouse.departmentmap.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.DepartmentMapId;
import com.warehouse.commonassets.repository.Criteria;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.departmentmap.application.port.secondary.DepartmentMapRepository;
import com.warehouse.departmentmap.domain.exception.DepartmentMapException;
import com.warehouse.departmentmap.domain.model.DepartmentMap;
import com.warehouse.departmentmap.infrastructure.adapter.secondary.entity.DepartmentMapEntity;
import com.warehouse.departmentmap.infrastructure.adapter.secondary.mapper.DepartmentMapEntityMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import org.hibernate.StaleStateException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public class DepartmentMapRepositoryImpl implements DepartmentMapRepository {

    private final OperatorFilteredRepository<DepartmentMapEntity> repository;
    private final EntityManager entityManager;

    public DepartmentMapRepositoryImpl(final OperatorFilteredRepository<DepartmentMapEntity> repository,
                                       final EntityManager entityManager) {
        this.repository = repository;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public DepartmentMap save(final DepartmentMap departmentMap) {
        final DepartmentMapEntity entity = DepartmentMapEntityMapper.toEntity(departmentMap);
        try {
            if (departmentMap.getVersion() == null) {
                this.repository.create(entity);
            } else {
                this.repository.update(entity);
            }
            this.entityManager.flush();
        } catch (final OptimisticLockException | StaleStateException exception) {
            throw DepartmentMapException.conflict("MAP_VERSION_CONFLICT", "Map " + departmentMap.getName()
                    + " was changed by another user. Reload the map and apply your changes again");
        }
        return byId(departmentMap.getDepartmentMapId()).one()
                .map(DepartmentMapEntityMapper::toModel)
                .orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DepartmentMap> findById(final DepartmentMapId departmentMapId) {
        return byId(departmentMapId).one().map(DepartmentMapEntityMapper::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentMap> findByDepartment(final DepartmentId departmentId) {
        return this.repository.createCriteria(DepartmentMapEntity.class)
                .eq("departmentId.value", departmentId.getValue())
                .asc("name")
                .list()
                .stream()
                .map(DepartmentMapEntityMapper::toModel)
                .toList();
    }

    @Override
    @Transactional
    public void delete(final DepartmentMapId departmentMapId) {
        byId(departmentMapId).one().ifPresent(this.repository::delete);
        this.entityManager.flush();
    }

    private Criteria<DepartmentMapEntity> byId(final DepartmentMapId departmentMapId) {
        return this.repository.createCriteria(DepartmentMapEntity.class)
                .eq("departmentMapId.value", departmentMapId.value());
    }
}
