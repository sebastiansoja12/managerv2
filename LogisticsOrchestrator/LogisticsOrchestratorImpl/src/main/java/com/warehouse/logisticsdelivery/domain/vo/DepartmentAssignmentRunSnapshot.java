package com.warehouse.logisticsdelivery.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentAssignmentRunId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.logisticsdelivery.domain.enumeration.CourierAssignmentDateField;
import com.warehouse.logisticsdelivery.domain.enumeration.DepartmentAssignmentRunStatus;

import java.time.Instant;

public record DepartmentAssignmentRunSnapshot(DepartmentAssignmentRunId runId,
                                              Long version,
                                              DepartmentId departmentId,
                                              CourierAssignmentDateField dateField,
                                              AssignmentWindow window,
                                              int batchSize,
                                              DepartmentAssignmentRunStatus status,
                                              DeliveryAssignmentCursor cursor,
                                              Instant maxEligibleAt,
                                              Instant watermark,
                                              int retries,
                                              String leaseOwner,
                                              Instant leaseUntil,
                                              AssignmentRunSummary summary,
                                              String error,
                                              Instant createdAt,
                                              Instant updatedAt) {
}
