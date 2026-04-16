package com.warehouse.department.infrastructure.adapter.secondary;

import java.util.List;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.department.domain.model.Department;
import com.warehouse.department.domain.port.secondary.DepartmentReadRepository;
import com.warehouse.department.domain.port.secondary.DepartmentRepository;
import com.warehouse.department.infrastructure.adapter.secondary.entity.DepartmentEntity;
import com.warehouse.department.infrastructure.adapter.secondary.entity.readmodel.DepartmentReadEntity;
import com.warehouse.department.infrastructure.adapter.secondary.mapper.DepartmentToEntityMapper;
import com.warehouse.department.infrastructure.adapter.secondary.mapper.DepartmentToModelMapper;

public class DepartmentRepositoryImpl implements DepartmentRepository {

    private final OperatorFilteredRepository<DepartmentEntity> repository;

    private final DepartmentReadRepository<DepartmentReadEntity> readRepository;

    public DepartmentRepositoryImpl(final OperatorFilteredRepository<DepartmentEntity> repository,
                                    final DepartmentReadRepository<DepartmentReadEntity> readRepository) {
        this.repository = repository;
        this.readRepository = readRepository;
    }

    @Override
    public Department findByDepartmentCode(final DepartmentCode departmentCode) {
        final DepartmentReadEntity department = readRepository.findById(departmentCode);
        return DepartmentToModelMapper.map(department);
    }

    @Override
    public List<Department> findAll() {
        final List<DepartmentReadEntity> departments = readRepository.list();
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
