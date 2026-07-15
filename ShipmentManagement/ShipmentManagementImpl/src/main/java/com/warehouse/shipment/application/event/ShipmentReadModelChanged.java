package com.warehouse.shipment.application.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.commonassets.event.integration.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.integration.context.OperatorAwareContext;
import com.warehouse.commonassets.event.integration.model.IntegrationEvent;
import com.warehouse.commonassets.event.integration.model.IntegrationEventKey;
import com.warehouse.shipment.application.event.snapshot.ShipmentReadModelData;

import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
@IntegrationEventType(value = "shipment.read-model.changed", version = 1)
public final class ShipmentReadModelChanged extends OperatorAwareContext
        implements IntegrationEvent, IntegrationEventKey {

    private final ShipmentReadModelData snapshot;
    private final Instant timestamp;

    @JsonCreator
    public ShipmentReadModelChanged(@JsonProperty("snapshot") final ShipmentReadModelData snapshot,
                                    @JsonProperty("timestamp") final Instant timestamp) {
        this.snapshot = snapshot;
        this.timestamp = timestamp;
    }

    public ShipmentReadModelData snapshot() {
        return snapshot;
    }

    public ShipmentReadModelData getSnapshot() {
        return snapshot;
    }

    public Instant timestamp() {
        return timestamp;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public String eventKey() {
        return String.valueOf(this.snapshot.shipmentId().getValue());
    }
}
