package com.warehouse.supplier.domain.event;

import com.warehouse.supplier.domain.vo.SupplierSnapshot;

import java.time.Instant;

public class SupplierDeleted extends SupplierChanged implements SupplierEvent {
    public SupplierDeleted(final SupplierSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }
}
