package com.warehouse.auth.infrastructure.adapter.secondary;

import com.warehouse.auth.domain.port.secondary.DepartmentServicePort;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.department.api.DepartmentApiService;


public class DepartmentServiceAdapter implements DepartmentServicePort {

    private final DepartmentApiService departmentApiService;

    public DepartmentServiceAdapter(final DepartmentApiService departmentApiService) {
        this.departmentApiService = departmentApiService;
    }

    @Override
    public Boolean departmentExists(final DepartmentCode departmentCode) {
        return this.departmentApiService.checkIfDepartmentExists(departmentCode);
    }
}
