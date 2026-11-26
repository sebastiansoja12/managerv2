package com.warehouse.logisticsdelivery.api.dto;

import com.warehouse.commonassets.identificator.DepartmentAssignmentRunId;
import com.warehouse.commonassets.identificator.DepartmentId;

import java.time.Instant;

public record DepartmentAssignmentRunDto(DepartmentAssignmentRunId runId,
                                         DepartmentId departmentId,
                                         Instant windowFrom,
                                         Instant windowTo,
                                         Instant backlogFrom,
                                         String status,
                                         int retries,
                                         Instant watermark,
                                         long discovered,
                                         long eligible,
                                         long assigned,
                                         long noCourier,
                                         long skippedNotAtDepartment,
                                         long skippedConcurrent,
                                         int batches,
                                         String error,
                                         Instant createdAt,
                                         Instant updatedAt) {
}
