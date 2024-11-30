package com.warehouse.delivery.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.delivery.domain.port.secondary.DepartmentRepository;
import com.warehouse.delivery.domain.vo.Department;

public class DepartmentRepositoryImpl implements DepartmentRepository {

    private final DepartmentReadRepository repository;

    public DepartmentRepositoryImpl(final DepartmentReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public Department findByCode(final DepartmentCode departmentCode) {
        return repository.findByDepotCode(departmentCode.getValue())
                .map(depotEntity -> new Department())
                .orElse(null);
    }
}
