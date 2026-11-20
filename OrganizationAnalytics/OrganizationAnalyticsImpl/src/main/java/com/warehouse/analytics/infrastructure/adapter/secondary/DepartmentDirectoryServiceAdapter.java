package com.warehouse.analytics.infrastructure.adapter.secondary;

import com.warehouse.analytics.application.port.secondary.DepartmentDirectoryServicePort;
import com.warehouse.analytics.domain.vo.AnalyticsDepartment;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.organisationstructure.department.api.DepartmentApiService;

import java.util.List;

public class DepartmentDirectoryServiceAdapter implements DepartmentDirectoryServicePort {

    private final DepartmentApiService departmentApiService;

    public DepartmentDirectoryServiceAdapter(final DepartmentApiService departmentApiService) {
        this.departmentApiService = departmentApiService;
    }

    @Override
    public List<AnalyticsDepartment> departments() {
        return this.departmentApiService.getAllDepartments().stream()
                .map(department -> new AnalyticsDepartment(new DepartmentId(department.departmentId()),
                        new DepartmentCode(department.departmentCode()), department.city()))
                .toList();
    }
}
