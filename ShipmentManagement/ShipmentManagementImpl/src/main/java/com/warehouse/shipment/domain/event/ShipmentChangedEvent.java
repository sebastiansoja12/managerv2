package com.warehouse.shipment.domain.event;

import com.warehouse.commonassets.kafka.domain.model.OperatorAwareContext;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public class ShipmentChangedEvent extends OperatorAwareContext implements ShipmentEvent {

    @NotNull
    private final ShipmentSnapshot snapshot;

    @NotNull
    private final Instant timestamp;

    public ShipmentChangedEvent(final ShipmentSnapshot snapshot, final Instant timestamp) {
        this.snapshot = snapshot;
        this.timestamp = timestamp;
    }

    public ShipmentSnapshot getSnapshot() {
        return snapshot;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

}
