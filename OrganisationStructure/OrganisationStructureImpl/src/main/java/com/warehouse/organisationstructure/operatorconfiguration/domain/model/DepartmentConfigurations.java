package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

import com.warehouse.commonassets.identificator.DepartmentId;

import java.util.Map;

public record DepartmentConfigurations(DepartmentConfiguration defaults,
                                       Map<DepartmentId, DepartmentConfiguration> departments) {

    public DepartmentConfigurations {
        departments = Map.copyOf(departments);
    }

    public DepartmentConfiguration forDepartment(final DepartmentId departmentId) {
        return departmentId == null ? defaults : departments.getOrDefault(departmentId, defaults);
    }
}
