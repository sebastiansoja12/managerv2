package com.warehouse.department.domain.event;

import com.warehouse.department.domain.vo.DepartmentSnapshot;

import java.time.Instant;

public class DepartmentTypeChanged implements DepartmentEvent {
    private final DepartmentSnapshot snapshot;
    private final Instant timestamp;

    public DepartmentTypeChanged(final DepartmentSnapshot snapshot, final Instant timestamp) {
        this.snapshot = snapshot;
        this.timestamp = timestamp;
    }

    public DepartmentSnapshot getSnapshot() {
        return snapshot;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
