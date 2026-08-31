package com.warehouse.organisationstructure.department.domain.event;

import com.warehouse.organisationstructure.department.domain.vo.DepartmentSnapshot;

import java.time.Instant;

public class DepartmentEmailChanged extends DepartmentChanged implements DepartmentEvent {
    public DepartmentEmailChanged(final DepartmentSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }
}
