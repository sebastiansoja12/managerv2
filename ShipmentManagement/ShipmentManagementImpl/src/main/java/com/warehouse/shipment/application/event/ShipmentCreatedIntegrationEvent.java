package com.warehouse.shipment.application.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.commonassets.event.domain.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.domain.model.IntegrationEvent;
import com.warehouse.shipment.application.event.snapshot.ShipmentSnapshot;

@IntegrationEventType(value = "shipment.created", version = 1)
public class ShipmentCreatedIntegrationEvent extends ShipmentChangedIntegrationEvent implements IntegrationEvent {

    @JsonCreator
    public ShipmentCreatedIntegrationEvent(@JsonProperty("payload") final ShipmentSnapshot payload) {
        super(payload);
    }
}
