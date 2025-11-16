package com.warehouse.department.domain.event;

import com.warehouse.department.domain.vo.DepartmentSnapshot;

import java.time.Instant;

public class DepartmentCreated extends DepartmentChanged implements DepartmentEvent {
    public DepartmentCreated(final DepartmentSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }
}
