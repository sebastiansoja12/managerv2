package com.warehouse.process.infrastructure.dto;

import java.time.Instant;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ServiceType;

public record ProcessStepDto(Instant createdAt,
                             ProcessType processType,
                             ServiceType serviceType,
                             String sourceService,
                             String targetService,
                             DepartmentCodeDto departmentCode,
                             String faultDescription) {
}
