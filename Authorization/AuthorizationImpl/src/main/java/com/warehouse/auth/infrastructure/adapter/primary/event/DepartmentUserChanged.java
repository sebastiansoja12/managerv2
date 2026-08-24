package com.warehouse.auth.infrastructure.adapter.primary.event;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DepartmentUserChanged(DepartmentCode departmentCode,
                                    UserId userId,
                                    String telephoneNumber,
                                    String email,
                                    Instant timestamp) {
}
