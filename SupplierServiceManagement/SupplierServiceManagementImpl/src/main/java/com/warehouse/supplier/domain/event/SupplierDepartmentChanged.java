package com.warehouse.supplier.domain.event;

import java.time.Instant;

import com.warehouse.supplier.domain.vo.SupplierSnapshot;

public class SupplierDepartmentChanged extends SupplierChanged implements SupplierEvent {
    public SupplierDepartmentChanged(final SupplierSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }
}
