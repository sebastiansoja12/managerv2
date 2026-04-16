package com.warehouse.dangerousgood.domain.event;

import com.warehouse.dangerousgood.domain.vo.GoodSnapshot;

import java.time.Instant;

public class GoodChangedEvent implements GoodEvent {
    private final GoodSnapshot snapshot;
    private final Instant timestamp;

    public GoodChangedEvent(final GoodSnapshot snapshot, final Instant timestamp) {
        this.snapshot = snapshot;
        this.timestamp = timestamp;
    }

    public GoodSnapshot getSnapshot() {
        return snapshot;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
