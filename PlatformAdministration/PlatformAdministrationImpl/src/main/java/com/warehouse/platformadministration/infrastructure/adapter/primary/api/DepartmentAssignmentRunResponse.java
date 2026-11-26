package com.warehouse.platformadministration.infrastructure.adapter.primary.api;

import java.time.Instant;
import java.util.UUID;

public record DepartmentAssignmentRunResponse(UUID runId,
                                              Long departmentId,
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
