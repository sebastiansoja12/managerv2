package com.warehouse.department.infrastructure.adapter.secondary.event;

import java.time.Instant;

import com.warehouse.commonassets.identificator.DepartmentCode;

public record DepartmentUserDeleted(DepartmentCode departmentCode,
                                    Instant timestamp) {
}
