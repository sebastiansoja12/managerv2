package com.warehouse.organisationstructure.api.dto;

import com.warehouse.commonassets.identificator.DepartmentId;

import java.util.Map;

public record DepartmentConfigurationsDto(DepartmentConfigurationDto defaults,
                                          Map<Long, DepartmentConfigurationDto> departments) {

    public DepartmentConfigurationsDto {
        departments = departments == null ? Map.of() : Map.copyOf(departments);
    }

    public DepartmentConfigurationDto forDepartment(final DepartmentId departmentId) {
        if (departmentId == null || departmentId.getValue() == null) {
            return defaults;
        }
        return departments.getOrDefault(departmentId.getValue(), defaults);
    }
}
