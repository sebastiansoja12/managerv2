package com.warehouse.process.infrastructure.dto;

import java.time.Instant;
import java.util.List;

public record ProcessDetailsDto(ProcessIdDto processId,
                                ProcessStatusDto status,
                                Instant createdAt,
                                Instant modifiedAt,
                                String faultDescription,
                                DeviceIdDto deviceId,
                                DepartmentCodeDto departmentCode,
                                UserIdDto userId,
                                List<ProcessStepDto> steps) {
}
