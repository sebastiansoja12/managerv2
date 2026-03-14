package com.warehouse.department.infrastructure.adapter.secondary;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.department.domain.model.Department;
import com.warehouse.department.domain.port.secondary.DepartmentRepository;
import com.warehouse.department.infrastructure.adapter.secondary.entity.DepartmentEntity;
import com.warehouse.department.infrastructure.adapter.secondary.mapper.DepartmentToEntityMapper;
import com.warehouse.department.infrastructure.adapter.secondary.mapper.DepartmentToModelMapper;

public class DepartmentRepositoryImpl implements DepartmentRepository {

    private final DepartmentReadRepository repository;

    public DepartmentRepositoryImpl(final DepartmentReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public Department findByDepartmentCode(final DepartmentCode departmentCode) {
		final DepartmentEntity department = repository
				.findByDepartmentCode(departmentCode)
				.orElse(null);
        return DepartmentToModelMapper.map(department);
    }

    @Override
    @Cacheable("departmentsCache")
    public List<Department> findAll() {
        final List<DepartmentEntity> departments = repository.findAll();
        return departments.stream().map(DepartmentToModelMapper::map).toList();
    }

    @Override
    public Boolean checkExists(final DepartmentCode departmentCode) {
        return repository.existsById(departmentCode);
    }

    @Override
    public void createOrUpdate(final Department department) {
        final DepartmentEntity departmentEntity = DepartmentToEntityMapper.map(department);
        this.repository.save(departmentEntity);
    }
}
