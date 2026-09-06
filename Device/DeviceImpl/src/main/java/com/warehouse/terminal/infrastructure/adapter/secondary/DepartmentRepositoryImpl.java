package com.warehouse.terminal.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.terminal.domain.model.Department;
import com.warehouse.terminal.domain.port.secondary.DepartmentRepository;
import com.warehouse.terminal.domain.port.secondary.DepartmentServicePort;

public class DepartmentRepositoryImpl implements DepartmentRepository {

    private final DepartmentServicePort departmentServicePort;

    public DepartmentRepositoryImpl(final DepartmentServicePort departmentServicePort) {
        this.departmentServicePort = departmentServicePort;
    }

    @Override
    public boolean existsByDepartmentCode(final DepartmentCode departmentCode) {
        try {
            return this.departmentServicePort.getDepartment(departmentCode) != null;
        } catch (final RuntimeException exception) {
            return false;
        }
    }

    @Override
    public Department findByDepartmentCode(final DepartmentCode departmentCode) {
        return this.departmentServicePort.getDepartment(departmentCode);
    }
}
