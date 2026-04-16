package com.warehouse.shipment.domain.event;

import com.warehouse.shipment.domain.vo.SignatureSnapshot;

import java.time.Instant;

public class SignatureChangedEvent {
    private final SignatureSnapshot snapshot;
    private final Instant timestamp;

    public SignatureChangedEvent(final SignatureSnapshot snapshot, final Instant timestamp) {
        this.snapshot = snapshot;
        this.timestamp = timestamp;
    }

    public SignatureSnapshot getSnapshot() {
        return snapshot;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
