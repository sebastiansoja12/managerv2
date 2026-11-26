package com.warehouse.logisticsdelivery.application.port.primary;

import com.warehouse.commonassets.identificator.DepartmentAssignmentRunId;
import com.warehouse.logisticsdelivery.application.port.primary.result.AssignmentOrchestration;
import com.warehouse.logisticsdelivery.domain.vo.DepartmentAssignmentRunSnapshot;

import java.util.List;

public interface DepartmentAssignmentPort {

    AssignmentOrchestration orchestrate();

    void process(final DepartmentAssignmentRunId runId);

    int recover();

    int cleanup();

    List<DepartmentAssignmentRunSnapshot> findLatestRuns(final int limit);
}
