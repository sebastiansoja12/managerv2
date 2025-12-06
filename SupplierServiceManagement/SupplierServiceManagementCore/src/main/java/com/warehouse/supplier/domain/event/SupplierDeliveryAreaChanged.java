package com.warehouse.supplier.domain.event;

import com.warehouse.supplier.domain.vo.SupplierSnapshot;

import java.time.Instant;

public class SupplierDeliveryAreaChanged extends SupplierChanged implements SupplierEvent {
    public SupplierDeliveryAreaChanged(final SupplierSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }
}
