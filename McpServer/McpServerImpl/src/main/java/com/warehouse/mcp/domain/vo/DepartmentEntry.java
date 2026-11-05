package com.warehouse.mcp.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;

public record DepartmentEntry(DepartmentId departmentId, DepartmentCode departmentCode, String type, String status) {
}
