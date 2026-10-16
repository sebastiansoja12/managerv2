package com.warehouse.organisationstructure.department.domain.event;

import com.warehouse.organisationstructure.department.domain.vo.DepartmentSnapshot;

import java.time.Instant;

public class DepartmentActivated extends DepartmentChanged implements DepartmentEvent {
    public DepartmentActivated(final DepartmentSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }
}
