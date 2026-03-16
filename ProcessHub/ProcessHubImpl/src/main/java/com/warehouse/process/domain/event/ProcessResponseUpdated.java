package com.warehouse.process.domain.event;

import java.time.Instant;

import com.warehouse.process.domain.vo.ProcessLogSnapshot;

public class ProcessResponseUpdated implements ProcessEvent {

    private final ProcessLogSnapshot snapshot;
    private final Instant timestamp;

    public ProcessResponseUpdated(final ProcessLogSnapshot snapshot, final Instant timestamp) {
        this.snapshot = snapshot;
        this.timestamp = timestamp;
    }

    public ProcessLogSnapshot getSnapshot() {
        return snapshot;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
