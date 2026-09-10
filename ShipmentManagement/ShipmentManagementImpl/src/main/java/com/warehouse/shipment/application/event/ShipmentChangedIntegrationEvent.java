package com.warehouse.shipment.application.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.commonassets.event.integration.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.integration.context.OperatorAwareContext;
import com.warehouse.commonassets.event.integration.model.IntegrationEvent;
import com.warehouse.commonassets.event.integration.model.IntegrationEventKey;
import com.warehouse.shipment.application.event.snapshot.ShipmentEventData;

@IntegrationEventType(value = "shipment.changed", version = 1)
public class ShipmentChangedIntegrationEvent extends OperatorAwareContext
        implements IntegrationEvent, IntegrationEventKey {

    private final ShipmentEventData payload;

    public ShipmentChangedIntegrationEvent(final ShipmentEventData shipmentEventData) {
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
