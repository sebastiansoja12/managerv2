package com.warehouse.organisationstructure.department.domain.event;

import com.warehouse.organisationstructure.department.domain.vo.DepartmentSnapshot;

import java.time.Instant;

public class DepartmentDeactivated extends DepartmentChanged implements DepartmentEvent {
    public DepartmentDeactivated(final DepartmentSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }
}
