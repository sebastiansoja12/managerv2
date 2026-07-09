package com.warehouse.returning.domain.event;

import com.warehouse.returning.domain.vo.ReturnPackageSnapshot;

import java.time.Instant;

public class ReturnPackageCreated extends ReturnPackageChanged implements ReturnPackageEvent {
    public ReturnPackageCreated(final ReturnPackageSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }
}
