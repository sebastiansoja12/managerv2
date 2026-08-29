package com.warehouse.shipment.application.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.commonassets.event.domain.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.domain.model.IntegrationEvent;
import com.warehouse.commonassets.kafka.domain.model.OperatorAwareContext;
import com.warehouse.shipment.application.event.snapshot.ShipmentSnapshot;

@IntegrationEventType(value = "shipment.changed", version = 1)
public class ShipmentChangedIntegrationEvent extends OperatorAwareContext implements IntegrationEvent {

    private final ShipmentSnapshot payload;

    @JsonCreator
    public ShipmentChangedIntegrationEvent(@JsonProperty("payload") final ShipmentSnapshot payload) {
        this.payload = payload;
    }

    public ShipmentSnapshot payload() {
        return payload;
    }

    public ShipmentSnapshot getPayload() {
        return payload;
    }
}
