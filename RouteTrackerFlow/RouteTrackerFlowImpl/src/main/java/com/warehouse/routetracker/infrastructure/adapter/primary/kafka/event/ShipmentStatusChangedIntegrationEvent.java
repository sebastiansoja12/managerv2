package com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class ShipmentStatusChangedIntegrationEvent extends ShipmentChangedIntegrationEvent {

    @JsonCreator
    public ShipmentStatusChangedIntegrationEvent(
            @JsonProperty("payload") final ShipmentChangedEventPayload payload) {
        super(payload);
    }
}
