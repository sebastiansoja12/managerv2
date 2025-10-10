package com.warehouse.shipment.infrastructure.adapter.primary.event;

import com.warehouse.dangerousgood.domain.vo.GoodSnapshot;

import java.time.Instant;

public class DangerousGoodChanged implements DangerousGoodEvent {
    private final GoodSnapshot snapshot;
    private final Instant timestamp;

    public DangerousGoodChanged(final GoodSnapshot snapshot, final Instant timestamp) {
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
