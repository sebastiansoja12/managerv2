package com.warehouse.logisticsdelivery.domain.service;

import com.warehouse.commonassets.identificator.DepartmentAssignmentRunId;
import com.warehouse.logisticsdelivery.domain.enumeration.DepartmentAssignmentRunStatus;
import com.warehouse.logisticsdelivery.domain.model.DepartmentAssignmentRun;
import com.warehouse.logisticsdelivery.domain.vo.AssignmentRunSummary;
import com.warehouse.logisticsdelivery.domain.vo.AssignmentWindow;
import com.warehouse.logisticsdelivery.domain.vo.DepartmentAssignmentConfig;

import java.time.Clock;
import java.time.Instant;

public class DepartmentAssignmentRunFactory {

    private final Clock clock;

    public DepartmentAssignmentRunFactory(final Clock clock) {
        this.clock = clock;
    }

    public DepartmentAssignmentRun create(final DepartmentAssignmentConfig config, final AssignmentWindow window) {
        final Instant now = this.clock.instant();
        return new DepartmentAssignmentRun(DepartmentAssignmentRunId.generate(), null, config.departmentId(),
                config.dateField(), window, config.batchSize(), DepartmentAssignmentRunStatus.PENDING, null, null,
                null, 0, null, null, AssignmentRunSummary.empty(), null, now, now);
    }
}
