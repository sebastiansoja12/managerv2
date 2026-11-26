package com.warehouse.shipment.api.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.eventoutbox.integration.annotation.IntegrationEventType;
import com.warehouse.eventoutbox.integration.model.IntegrationEvent;
import com.warehouse.shipment.api.event.snapshot.ShipmentIncidentData;

@JsonIgnoreProperties(ignoreUnknown = true)
@IntegrationEventType(value = "shipment.incident.commented", version = 1, topic = ShipmentTopics.INCIDENT_EVENTS)
public class IncidentCommented extends IncidentChanged implements IntegrationEvent {

    @JsonCreator
    public IncidentCommented(@JsonProperty("payload") final ShipmentIncidentData payload) {
        super(payload);
    }
}
