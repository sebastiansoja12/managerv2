package com.warehouse.logisticsdelivery.application.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentAssignmentRunId;

public interface AssignmentRunExecutionServicePort {

    void submit(final DepartmentAssignmentRunId runId);
}
