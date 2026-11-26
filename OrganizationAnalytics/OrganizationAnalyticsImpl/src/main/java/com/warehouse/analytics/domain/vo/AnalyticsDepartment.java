package com.warehouse.analytics.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;

public record AnalyticsDepartment(DepartmentId departmentId, DepartmentCode departmentCode, String city) {
}
