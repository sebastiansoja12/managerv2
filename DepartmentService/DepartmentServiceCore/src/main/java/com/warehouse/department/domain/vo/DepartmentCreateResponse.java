package com.warehouse.department.domain.vo;

import com.warehouse.department.domain.model.Department;

import java.util.Map;

public record DepartmentCreateResponse(Map<Department, Boolean> departmentMap) {
}
