package com.warehouse.shipment.api.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.commonassets.event.integration.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.integration.context.OperatorAwareContext;
import com.warehouse.commonassets.event.integration.model.IntegrationEvent;
import com.warehouse.commonassets.event.integration.model.IntegrationEventKey;
import com.warehouse.shipment.api.event.snapshot.ShipmentEventData;

@JsonIgnoreProperties(ignoreUnknown = true)
@IntegrationEventType(value = "shipment.changed", version = 1)
public class ShipmentChangedIntegrationEvent extends OperatorAwareContext
        implements IntegrationEvent, IntegrationEventKey {

    private final ShipmentEventData payload;

    @JsonCreator
    public ShipmentChangedIntegrationEvent(@JsonProperty("payload") final ShipmentEventData shipmentEventData) {
        this.payload = shipmentEventData;
    }

    @JsonProperty("payload")
    public ShipmentEventData payload() {
        return payload;
    }

    @Override
    public String eventKey() {
        return String.valueOf(this.payload.shipmentId().getValue());
    }
}
