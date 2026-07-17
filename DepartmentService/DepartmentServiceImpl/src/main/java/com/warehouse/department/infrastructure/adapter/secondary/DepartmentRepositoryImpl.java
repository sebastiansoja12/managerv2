package com.warehouse.department.infrastructure.adapter.secondary;

import java.util.List;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.department.domain.model.Department;
import com.warehouse.department.domain.port.secondary.DepartmentRepository;
import com.warehouse.department.infrastructure.adapter.secondary.entity.DepartmentEntity;
import com.warehouse.department.infrastructure.adapter.secondary.mapper.DepartmentToEntityMapper;
import com.warehouse.department.infrastructure.adapter.secondary.mapper.DepartmentToModelMapper;

public class DepartmentRepositoryImpl implements DepartmentRepository {

    private static final List<DepartmentEntity.Status> EXCLUDED_STATUSES = List.of(
            DepartmentEntity.Status.ARCHIVED,
            DepartmentEntity.Status.DELETED
    );

    private final OperatorFilteredRepository<DepartmentEntity> repository;

    public DepartmentRepositoryImpl(final OperatorFilteredRepository<DepartmentEntity> repository) {
        this.repository = repository;
    }

    @Override
    public Department findByDepartmentCode(final DepartmentCode departmentCode) {
        final DepartmentEntity department = repository.createCriteria(DepartmentEntity.class)
                .eq("departmentCode.value", departmentCode)
                .notIn("status", EXCLUDED_STATUSES)
                .one()
                .orElse(null);
        return DepartmentToModelMapper.map(department);
    }

    @Override
    public List<Department> findAll() {
        final List<DepartmentEntity> departments = repository.createCriteria(DepartmentEntity.class)
                .notIn("status", EXCLUDED_STATUSES)
                .list();
        return departments.stream().map(DepartmentToModelMapper::map).toList();
    }

    @Override
    public Boolean checkExists(final DepartmentCode departmentCode) {
        return findByDepartmentCode(departmentCode) != null;
    }

    @Override
    public void createOrUpdate(final Department department) {
        final DepartmentEntity departmentEntity = DepartmentToEntityMapper.map(department);
        if (existsIncludingExcludedStatuses(department.getDepartmentCode())) {
            this.repository.update(departmentEntity);
        } else {
            this.repository.create(departmentEntity);
        }
    }

    private boolean existsIncludingExcludedStatuses(final DepartmentCode departmentCode) {
        return repository.createCriteria(DepartmentEntity.class)
                .eq("departmentCode.value", departmentCode)
                .one()
                .isPresent();
    }
}
