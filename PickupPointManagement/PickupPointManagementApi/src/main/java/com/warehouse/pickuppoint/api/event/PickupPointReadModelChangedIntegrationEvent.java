package com.warehouse.pickuppoint.api.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.commonassets.event.integration.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.integration.context.OperatorAwareContext;
import com.warehouse.commonassets.event.integration.model.IntegrationEvent;
import com.warehouse.commonassets.event.integration.model.IntegrationEventKey;
import com.warehouse.pickuppoint.api.event.snapshot.PickupPointReadModelData;

import java.time.Instant;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@IntegrationEventType(value = "pickup-point.read-model.changed", version = 1)
public final class PickupPointReadModelChangedIntegrationEvent extends OperatorAwareContext
        implements IntegrationEvent, IntegrationEventKey {

    private final PickupPointReadModelData snapshot;
    private final Instant timestamp;

    @JsonCreator
    public PickupPointReadModelChangedIntegrationEvent(
            @JsonProperty("snapshot") final PickupPointReadModelData snapshot,
            @JsonProperty("timestamp") final Instant timestamp) {
        this.snapshot = Objects.requireNonNull(snapshot, "Pickup point read model data cannot be null");
        this.timestamp = Objects.requireNonNull(timestamp, "Timestamp cannot be null");
    }

    public PickupPointReadModelData snapshot() {
        return this.snapshot;
    }

    public PickupPointReadModelData getSnapshot() {
        return this.snapshot;
    }

    public Instant timestamp() {
        return this.timestamp;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

    @Override
    public String eventKey() {
        return this.snapshot.pickupPointId().value().toString();
    }
}
