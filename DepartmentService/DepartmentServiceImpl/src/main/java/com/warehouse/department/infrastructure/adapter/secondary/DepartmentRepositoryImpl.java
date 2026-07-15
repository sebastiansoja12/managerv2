package com.warehouse.department.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.department.domain.model.Department;
import com.warehouse.department.domain.port.secondary.DepartmentReadRepository;
import com.warehouse.department.domain.port.secondary.DepartmentRepository;
import com.warehouse.department.infrastructure.adapter.secondary.entity.DepartmentEntity;
import com.warehouse.department.infrastructure.adapter.secondary.entity.readmodel.DepartmentReadEntity;
import com.warehouse.department.infrastructure.adapter.secondary.mapper.DepartmentToEntityMapper;
import com.warehouse.department.infrastructure.adapter.secondary.mapper.DepartmentToModelMapper;

import java.util.List;

public class DepartmentRepositoryImpl implements DepartmentRepository {

    private static final List<DepartmentEntity.Status> EXCLUDED_STATUSES = List.of(
            DepartmentEntity.Status.ARCHIVED,
            DepartmentEntity.Status.DELETED
    );

    private final OperatorFilteredRepository<DepartmentEntity> repository;

    private final DepartmentReadRepository<DepartmentReadEntity> readRepository;

    public DepartmentRepositoryImpl(final OperatorFilteredRepository<DepartmentEntity> repository,
                                    final DepartmentReadRepository<DepartmentReadEntity> readRepository) {
        this.repository = repository;
        this.readRepository = readRepository;
    }

    @Override
    public Department findByDepartmentCode(final DepartmentCode departmentCode) {
        return repository.createCriteria(DepartmentEntity.class)
                .eq("departmentCode.value", departmentCode)
                .notIn("status", EXCLUDED_STATUSES)
                .one()
                .map(DepartmentToModelMapper::map)
                .orElse(null);
    }

    @Override
    public Department findByDepartmentCodeIncludingArchived(final DepartmentCode departmentCode) {
        return repository.createCriteria(DepartmentEntity.class)
                .eq("departmentCode.value", departmentCode)
                .notIn("status", List.of(DepartmentEntity.Status.DELETED))
                .one()
                .map(DepartmentToModelMapper::map)
                .orElse(null);
    }

    @Override
    public Department findByDepartmentId(final DepartmentId departmentId) {
        return repository.createCriteria(DepartmentEntity.class)
                .eq("departmentId.value", departmentId.getValue())
                .notIn("status", EXCLUDED_STATUSES)
                .one()
                .map(DepartmentToModelMapper::map)
                .orElse(null);
    }

    @Override
    public List<Department> findAll() {
        final List<DepartmentReadEntity> departments = readRepository.list();
        return departments.stream().map(DepartmentToModelMapper::map).toList();
    }

    @Override
    public List<Department> findAllArchived() {
        final List<DepartmentReadEntity> departments = readRepository.listArchived();
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
            department.assignDepartmentId(departmentEntity.getDepartmentId());
        }
    }

    private boolean existsIncludingExcludedStatuses(final DepartmentCode departmentCode) {
        return repository.createCriteria(DepartmentEntity.class)
                .eq("departmentCode.value", departmentCode)
                .one()
                .isPresent();
    }
}
