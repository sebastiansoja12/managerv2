package com.warehouse.department.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentCode;

public record ChangeDepartmentStatusCommand(DepartmentCode departmentCode, String status) {
}
