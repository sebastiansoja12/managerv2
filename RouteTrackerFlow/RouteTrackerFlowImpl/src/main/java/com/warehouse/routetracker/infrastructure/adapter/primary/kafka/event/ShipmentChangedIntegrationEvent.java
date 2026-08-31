package com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ShipmentChangedIntegrationEvent extends OperatorAwareContext {

    public static final String TYPE = "shipment.changed";

    private final ShipmentChangedEventPayload payload;

    @JsonCreator
    public ShipmentChangedIntegrationEvent(
            @JsonProperty("payload") final ShipmentChangedEventPayload payload) {
        super(payload.userId(), payload.departmentId(), payload.operatorId());
        this.payload = payload;
    }

    public ShipmentChangedEventPayload payload() {
        return payload;
    }
}
