package com.warehouse.department.domain.event;

import java.time.Instant;

import com.warehouse.department.domain.vo.DepartmentSnapshot;

public class DepartmentArchived extends DepartmentChanged implements DepartmentEvent {
    public DepartmentArchived(final DepartmentSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }
}
