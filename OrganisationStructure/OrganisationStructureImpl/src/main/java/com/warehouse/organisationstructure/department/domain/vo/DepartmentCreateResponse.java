package com.warehouse.organisationstructure.department.domain.vo;

import com.warehouse.organisationstructure.department.domain.model.Department;

import java.util.Map;

public record DepartmentCreateResponse(Map<Department, Boolean> departmentMap) {
}
