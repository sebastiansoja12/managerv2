package com.warehouse.shipment.api.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.commonassets.event.integration.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.integration.model.IntegrationEvent;
import com.warehouse.shipment.api.event.snapshot.ShipmentIncidentData;

@JsonIgnoreProperties(ignoreUnknown = true)
@IntegrationEventType(value = "shipment.incident.commented", version = 1)
public class IncidentCommented extends IncidentChanged implements IntegrationEvent {

    @JsonCreator
    public IncidentCommented(@JsonProperty("payload") final ShipmentIncidentData payload) {
        super(payload);
    }
}
