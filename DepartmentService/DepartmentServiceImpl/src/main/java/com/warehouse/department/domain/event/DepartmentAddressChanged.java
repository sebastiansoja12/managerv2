package com.warehouse.department.domain.event;

import com.warehouse.department.domain.vo.DepartmentSnapshot;

import java.time.Instant;

public class DepartmentAddressChanged extends DepartmentChanged implements DepartmentEvent {
    public DepartmentAddressChanged(final DepartmentSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }
}
