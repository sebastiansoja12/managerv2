package com.warehouse.department.infrastructure.adapter.secondary.event;

import java.time.Instant;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;

public record DepartmentUserChanged(DepartmentCode departmentCode,
                                    UserId userId,
                                    String telephoneNumber,
                                    String email,
                                    Instant timestamp) {
}
