package com.warehouse.logistics.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.logistics.domain.port.secondary.DepartmentRepository;
import com.warehouse.logistics.domain.vo.Department;

public class DepartmentRepositoryImpl implements DepartmentRepository {

    private final DepartmentReadRepository repository;

    public DepartmentRepositoryImpl(final DepartmentReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public Department findByCode(final DepartmentCode departmentCode) {
        return repository.findByDepartmentCode(departmentCode.getValue())
                .map(department -> new Department(department.isActive()))
                .orElse(null);
    }
}
