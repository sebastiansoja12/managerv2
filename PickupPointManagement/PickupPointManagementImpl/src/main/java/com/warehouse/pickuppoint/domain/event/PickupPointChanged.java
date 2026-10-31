package com.warehouse.pickuppoint.domain.event;

import com.warehouse.pickuppoint.domain.vo.PickupPointSnapshot;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

public abstract class PickupPointChanged implements PickupPointEvent {

    private final PickupPointSnapshot snapshot;
    private final Instant timestamp;
    private final PickupPointSnapshot previousSnapshot;

    protected PickupPointChanged(final PickupPointSnapshot snapshot, final Instant timestamp) {
        this(null, snapshot, timestamp);
    }

    protected PickupPointChanged(final PickupPointSnapshot previousSnapshot,
                                 final PickupPointSnapshot snapshot,
                                 final Instant timestamp) {
        this.previousSnapshot = previousSnapshot;
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

    public Optional<PickupPointSnapshot> getPreviousSnapshot() {
        return Optional.ofNullable(this.previousSnapshot);
    }
}
