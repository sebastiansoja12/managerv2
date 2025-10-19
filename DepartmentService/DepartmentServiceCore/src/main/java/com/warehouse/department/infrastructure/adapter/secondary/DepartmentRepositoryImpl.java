package com.warehouse.department.infrastructure.adapter.secondary;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import com.warehouse.department.domain.model.Department;
import com.warehouse.department.domain.port.secondary.DepartmentRepository;
import com.warehouse.department.domain.vo.DepartmentCode;
import com.warehouse.department.infrastructure.adapter.secondary.entity.DepartmentEntity;
import com.warehouse.department.infrastructure.adapter.secondary.mapper.DepartmentToEntityMapper;
import com.warehouse.department.infrastructure.adapter.secondary.mapper.DepartmentToModelMapper;

public class DepartmentRepositoryImpl implements DepartmentRepository {

    private final DepartmentReadRepository repository;

    public DepartmentRepositoryImpl(final DepartmentReadRepository repository) {
        this.repository = repository;
    }

    @Override
    @Cacheable("departmentCodeCache")
    public Department findByCode(final DepartmentCode departmentCode) {
		final DepartmentEntity department = repository
				.findByDepartmentCode(
						new com.warehouse.commonassets.identificator.DepartmentCode(departmentCode.getValue()))
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
    public void createOrUpdate(final Department department) {
        final DepartmentEntity departmentEntity = DepartmentToEntityMapper.map(department);
        this.repository.save(departmentEntity);
    }
}
