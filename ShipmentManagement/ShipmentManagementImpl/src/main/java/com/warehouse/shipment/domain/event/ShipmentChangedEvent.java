package com.warehouse.shipment.domain.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.commonassets.kafka.domain.annotation.KafkaDomainEvent;
import com.warehouse.commonassets.kafka.domain.model.KafkaEventKey;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

@KafkaDomainEvent(
        topicProperty = "manager.kafka.topics.shipment-read-model-sync",
        topic = "shipment.read-model.sync"
)
public class ShipmentChangedEvent implements KafkaEventKey {

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

    @Override
    public String kafkaKey() {
        return String.valueOf(snapshot.shipmentId().getValue());
    }
}
