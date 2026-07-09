package com.warehouse.returning.domain.event;

import java.time.Instant;

import com.warehouse.returning.domain.vo.ReturnPackageSnapshot;

public class ReturnPackageCanceled extends ReturnPackageChanged implements ReturnPackageEvent {
    public ReturnPackageCanceled(final ReturnPackageSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }
}
