package com.warehouse.supplier.domain.event;

import com.warehouse.supplier.domain.vo.SupplierSnapshot;

import java.time.Instant;

public class SupplierActivated extends SupplierChanged implements SupplierEvent {
    public SupplierActivated(final SupplierSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }
}
