package com.warehouse.logisticsdelivery.domain.event;

import com.warehouse.logisticsdelivery.domain.vo.DeliverySnapshot;

import java.time.Instant;
import java.util.Objects;

public class DeliveryReadModelRebuilt implements DeliveryEvent {

    private final DeliverySnapshot snapshot;

    private final Instant timestamp;

    public DeliveryReadModelRebuilt(final DeliverySnapshot snapshot, final Instant timestamp) {
        this.snapshot = Objects.requireNonNull(snapshot, "Delivery snapshot cannot be null");
        this.timestamp = Objects.requireNonNull(timestamp, "Timestamp cannot be null");
    }

    @Override
    public DeliverySnapshot getSnapshot() {
        return this.snapshot;
    }

    @Override
    public Instant getTimestamp() {
        return this.timestamp;
    }
}
