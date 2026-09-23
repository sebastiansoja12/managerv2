package com.warehouse.returning.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.department.api.DepartmentApiService;
import com.warehouse.returning.application.port.secondary.DepartmentServicePort;
import org.springframework.stereotype.Component;

@Component("returningDepartmentServiceAdapter")
public class DepartmentServiceAdapter implements DepartmentServicePort {
    private final DepartmentApiService departmentApiService;

    public DepartmentServiceAdapter(final DepartmentApiService departmentApiService) {
        this.departmentApiService = departmentApiService;
    }

    @Override
    public boolean exists(final DepartmentId departmentId) {
        return departmentApiService.checkIfDepartmentExists(departmentId);
    }

    @Override
    public String getCode(final DepartmentId departmentId) {
        return departmentApiService.getDepartmentById(departmentId).departmentCode();
    }
}
