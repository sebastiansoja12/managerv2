package com.warehouse.platformadministration.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentAssignmentRunId;
import com.warehouse.commonassets.identificator.DepartmentId;

import java.time.Instant;

public record DepartmentAssignmentRunView(DepartmentAssignmentRunId runId,
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
