package com.warehouse.shipment.api.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.eventoutbox.integration.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.integration.context.OperatorAwareContext;
import com.warehouse.eventoutbox.integration.model.IntegrationEvent;
import com.warehouse.eventoutbox.integration.model.IntegrationEventKey;
import com.warehouse.shipment.api.event.snapshot.ShipmentIncidentData;

@JsonIgnoreProperties(ignoreUnknown = true)
@IntegrationEventType(value = "shipment.incident.changed", version = 1, topic = ShipmentTopics.INCIDENT_EVENTS)
public class IncidentChanged extends OperatorAwareContext implements IntegrationEvent, IntegrationEventKey {

    private final ShipmentIncidentData payload;

    @JsonCreator
    public IncidentChanged(@JsonProperty("payload") final ShipmentIncidentData payload) {
        this.payload = payload;
    }

    @JsonProperty("payload")
    public ShipmentIncidentData payload() {
        return payload;
    }

    @Override
    public String eventKey() {
        return String.valueOf(this.payload.shipmentId().getValue());
    }
}
