package com.warehouse.returning.domain.event;

import com.warehouse.returning.domain.vo.ReturnPackageSnapshot;

import java.time.Instant;

public class ReturnPackageChanged implements ReturnPackageEvent {
    private final ReturnPackageSnapshot snapshot;
    private final Instant timestamp;

    public ReturnPackageChanged(final ReturnPackageSnapshot snapshot, final Instant timestamp) {
        this.snapshot = snapshot;
        this.timestamp = timestamp;
    }

    public ReturnPackageSnapshot getSnapshot() {
        return snapshot;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
