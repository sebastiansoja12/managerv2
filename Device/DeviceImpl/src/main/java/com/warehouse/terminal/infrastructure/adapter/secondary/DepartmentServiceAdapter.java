package com.warehouse.terminal.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.department.api.DepartmentApiService;
import com.warehouse.department.api.dto.DepartmentDto;
import com.warehouse.terminal.domain.model.Department;
import com.warehouse.terminal.domain.port.secondary.DepartmentServicePort;

public class DepartmentServiceAdapter implements DepartmentServicePort {

    private final DepartmentApiService departmentApiService;

    public DepartmentServiceAdapter(final DepartmentApiService departmentApiService) {
        this.departmentApiService = departmentApiService;
    }

    @Override
    public Department getDepartment(final DepartmentCode departmentCode) {
        final DepartmentDto department = this.departmentApiService.getDepartmentByCode(departmentCode);
        return Department.from(department);
    }
}
