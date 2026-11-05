package com.warehouse.departmentmap.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;

public record DepartmentDirection(DepartmentId departmentId, DepartmentCode departmentCode) {
}
