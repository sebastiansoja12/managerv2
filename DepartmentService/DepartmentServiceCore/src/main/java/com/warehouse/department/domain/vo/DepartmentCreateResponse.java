package com.warehouse.department.domain.vo;

import java.util.Map;

public record DepartmentCreateResponse(Map<DepartmentCode, Boolean> departmentMap) {
}
