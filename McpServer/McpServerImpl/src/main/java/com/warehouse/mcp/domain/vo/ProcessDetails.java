package com.warehouse.mcp.domain.vo;

import java.time.Instant;
import java.util.List;

import com.warehouse.commonassets.identificator.ProcessId;

public record ProcessDetails(ProcessId processId,
                             String status,
                             Instant createdAt,
                             Instant modifiedAt,
                             String faultDescription,
                             String deviceId,
                             String departmentCode,
                             Long userId,
                             List<ProcessStep> steps) {
}
