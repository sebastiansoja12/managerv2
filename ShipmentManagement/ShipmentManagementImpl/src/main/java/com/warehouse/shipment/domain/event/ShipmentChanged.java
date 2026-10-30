package com.warehouse.shipment.domain.event;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.Optional;

public class ShipmentChanged implements ShipmentEvent {

    @NotNull
    private final ShipmentSnapshot snapshot;

    @NotNull
    private final Instant timestamp;

    private final ShipmentSnapshot previousSnapshot;

    public ShipmentChanged(final ShipmentSnapshot snapshot, final Instant timestamp) {
        this(null, snapshot, timestamp);
    }

    public ShipmentChanged(final ShipmentSnapshot previousSnapshot,
                           final ShipmentSnapshot snapshot,
                           final Instant timestamp) {
        this.previousSnapshot = previousSnapshot;
        this.snapshot = snapshot;
        this.timestamp = timestamp;
    }

    public ShipmentSnapshot getSnapshot() {
        return snapshot;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Optional<ShipmentSnapshot> getPreviousSnapshot() {
        return Optional.ofNullable(previousSnapshot);
    }

    public ShipmentId shipmentId() {
        return snapshot.shipmentId();
    }

}
