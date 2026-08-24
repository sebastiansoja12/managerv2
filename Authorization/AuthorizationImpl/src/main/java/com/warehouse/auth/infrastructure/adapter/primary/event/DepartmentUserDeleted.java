package com.warehouse.auth.infrastructure.adapter.primary.event;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.warehouse.commonassets.identificator.DepartmentCode;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DepartmentUserDeleted(DepartmentCode departmentCode,
                                    Instant timestamp) {
}
