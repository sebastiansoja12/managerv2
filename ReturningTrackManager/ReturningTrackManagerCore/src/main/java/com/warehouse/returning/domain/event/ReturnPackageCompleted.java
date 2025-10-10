package com.warehouse.returning.domain.event;

import java.time.Instant;

import com.warehouse.returning.domain.vo.ReturnPackageSnapshot;

public class ReturnPackageCompleted extends ReturnPackageChanged implements ReturnPackageEvent {
    public ReturnPackageCompleted(final ReturnPackageSnapshot snapshot, final Instant timestamp) {
        super(snapshot, timestamp);
    }
}
