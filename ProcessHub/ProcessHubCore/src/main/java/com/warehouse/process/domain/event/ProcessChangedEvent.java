package com.warehouse.process.domain.event;

import java.time.Instant;

import com.warehouse.process.domain.vo.ProcessLogSnapshot;

public class ProcessChangedEvent implements ProcessEvent {

    private final ProcessLogSnapshot snapshot;
    private final Instant occurredAt;

    public ProcessChangedEvent(final ProcessLogSnapshot snapshot,
                               final Instant occurredAt) {
        this.snapshot = snapshot;
        this.occurredAt = occurredAt;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public ProcessLogSnapshot getSnapshot() {
        return snapshot;
    }
}
