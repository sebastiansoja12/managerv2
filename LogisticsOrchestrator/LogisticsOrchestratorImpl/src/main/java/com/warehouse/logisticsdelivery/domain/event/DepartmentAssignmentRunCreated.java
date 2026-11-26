package com.warehouse.logisticsdelivery.domain.event;

import com.warehouse.commonassets.identificator.DepartmentAssignmentRunId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.eventoutbox.domain.model.DomainEvent;

import java.time.Instant;

public record DepartmentAssignmentRunCreated(DepartmentAssignmentRunId runId,
                                             DepartmentId departmentId,
                                             Instant timestamp) implements DomainEvent {

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }
}
