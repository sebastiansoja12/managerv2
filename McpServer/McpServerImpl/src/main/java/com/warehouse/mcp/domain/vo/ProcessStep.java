package com.warehouse.mcp.domain.vo;

import java.time.Instant;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ServiceType;

public record ProcessStep(Instant createdAt,
                          ProcessType processType,
                          ServiceType serviceType,
                          String sourceService,
                          String targetService,
                          String departmentCode,
                          String faultDescription) {
}
