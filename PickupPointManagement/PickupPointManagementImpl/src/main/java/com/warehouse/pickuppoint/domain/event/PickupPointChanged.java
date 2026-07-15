package com.warehouse.pickuppoint.domain.event;

import com.warehouse.pickuppoint.domain.vo.PickupPointSnapshot;

import java.time.Instant;
import java.util.Objects;

public abstract class PickupPointChanged implements PickupPointEvent {

    private final PickupPointSnapshot snapshot;
    private final Instant timestamp;

    protected PickupPointChanged(final PickupPointSnapshot snapshot, final Instant timestamp) {
        this.snapshot = Objects.requireNonNull(snapshot, "Pickup point snapshot cannot be null");
        this.timestamp = Objects.requireNonNull(timestamp, "Timestamp cannot be null");
    }

    @Override
    public PickupPointSnapshot getSnapshot() {
        return this.snapshot;
    }

    @Override
    public Instant getTimestamp() {
        return this.timestamp;
    }
}
