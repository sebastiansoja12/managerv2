package com.warehouse.shipment.api.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.eventoutbox.integration.annotation.IntegrationEventType;
import com.warehouse.eventoutbox.integration.model.IntegrationEvent;
import com.warehouse.shipment.api.event.snapshot.ShipmentEventData;

@JsonIgnoreProperties(ignoreUnknown = true)
@IntegrationEventType(value = "shipment.destination.changed", version = 1, topic = ShipmentTopics.EVENTS)
public class ShipmentDestinationChangedIntegrationEvent extends ShipmentChangedIntegrationEvent implements IntegrationEvent {

    @JsonCreator
    public ShipmentDestinationChangedIntegrationEvent(@JsonProperty("payload") final ShipmentEventData shipmentEventData) {
        super(shipmentEventData);
    }
}
