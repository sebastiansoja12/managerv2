package com.warehouse.logisticsdelivery.application.port.secondary;

import com.warehouse.logisticsdelivery.domain.vo.DepartmentAssignmentRunSnapshot;

import java.time.Duration;

public interface AssignmentRunMetrics {

    void recordRun(final DepartmentAssignmentRunSnapshot run, final Duration duration);

    void recordOrchestration(final int triggeredDepartments, final int skippedDepartments);

    void recordRecovery(final int redispatchedRuns);
}
