package com.warehouse.departmentmap.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.departmentmap.application.port.secondary.DepartmentServicePort;
import com.warehouse.organisationstructure.department.api.DepartmentApiService;

public class DepartmentServiceAdapter implements DepartmentServicePort {

    private final DepartmentApiService departmentApiService;

    public DepartmentServiceAdapter(final DepartmentApiService departmentApiService) {
        this.departmentApiService = departmentApiService;
    }

    @Override
    public boolean departmentExists(final DepartmentId departmentId) {
        return Boolean.TRUE.equals(this.departmentApiService.checkIfDepartmentExists(departmentId));
    }
}
