package com.warehouse.deliverynetwork.domain.event;

import com.warehouse.deliverynetwork.domain.vo.DeliveryNetworkSnapshot;

import java.time.Instant;
import java.util.Objects;

public class DeliveryNetworkChanged implements DeliveryNetworkEvent {

    private final DeliveryNetworkSnapshot snapshot;

    private final Instant timestamp;

    public DeliveryNetworkChanged(final DeliveryNetworkSnapshot snapshot, final Instant timestamp) {
        this.snapshot = Objects.requireNonNull(snapshot, "Delivery network snapshot cannot be null");
        this.timestamp = Objects.requireNonNull(timestamp, "Timestamp cannot be null");
    }

    @Override
    public DeliveryNetworkSnapshot getSnapshot() {
        return this.snapshot;
    }

    @Override
    public Instant getTimestamp() {
        return this.timestamp;
    }
}
