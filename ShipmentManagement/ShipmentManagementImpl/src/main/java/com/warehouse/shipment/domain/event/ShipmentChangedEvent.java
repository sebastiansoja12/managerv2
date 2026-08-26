package com.warehouse.shipment.domain.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.commonassets.kafka.domain.model.OperatorAwareContext;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public class ShipmentChangedEvent extends OperatorAwareContext {

    @NotNull
    private final ShipmentSnapshot snapshot;

    @NotNull
    private final Instant timestamp;

    @JsonCreator
    public ShipmentChangedEvent(@JsonProperty("snapshot") final ShipmentSnapshot snapshot,
                                @JsonProperty("timestamp") final Instant timestamp) {
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
