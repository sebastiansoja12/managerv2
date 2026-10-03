package com.warehouse.shipment.api.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.commonassets.event.integration.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.integration.model.IntegrationEvent;
import com.warehouse.shipment.api.event.snapshot.ShipmentEventData;

@IntegrationEventType(value = "shipment.created", version = 1)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShipmentCreatedIntegrationEvent extends ShipmentChangedIntegrationEvent implements IntegrationEvent {

    @JsonCreator
    public ShipmentCreatedIntegrationEvent(@JsonProperty("payload") final ShipmentEventData shipmentEventData) {
        super(shipmentEventData);
    }
}
