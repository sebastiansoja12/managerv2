package com.warehouse.department.domain.event;

import java.time.Instant;

import com.warehouse.department.domain.vo.DepartmentSnapshot;

public class DepartmentSuspended extends DepartmentChanged implements DepartmentEvent {
    public DepartmentSuspended(final DepartmentSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }
}
