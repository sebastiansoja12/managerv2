package com.warehouse.department.domain.event;

import com.warehouse.department.domain.vo.DepartmentSnapshot;

import java.time.Instant;

public class DepartmentTaxIdChanged extends DepartmentChanged implements DepartmentEvent {
    public DepartmentTaxIdChanged(final DepartmentSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }
}
