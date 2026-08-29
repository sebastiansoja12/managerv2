package com.warehouse.shipment.infrastructure.adapter.secondary.kafka.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.commonassets.event.domain.model.IntegrationEvent;
import com.warehouse.commonassets.kafka.domain.model.OperatorAwareContext;
import com.warehouse.commonassets.kafka.infrastructure.annotation.KafkaTopic;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
@KafkaTopic("${manager.kafka.topics.shipment-read-model-sync:shipment.read-model.sync}")
public final class ShipmentReadModelChanged extends OperatorAwareContext implements IntegrationEvent {

    private final ShipmentSnapshot snapshot;
    private final Instant timestamp;

    @JsonCreator
    public ShipmentReadModelChanged(@JsonProperty("snapshot") final ShipmentSnapshot snapshot,
                                    @JsonProperty("timestamp") final Instant timestamp) {
        this.snapshot = snapshot;
        this.timestamp = timestamp;
    }

    public ShipmentSnapshot snapshot() {
        return snapshot;
    }

    public ShipmentSnapshot getSnapshot() {
        return snapshot;
    }

    public Instant timestamp() {
        return timestamp;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
