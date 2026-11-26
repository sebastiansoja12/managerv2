package com.warehouse.shipment.api.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.eventoutbox.integration.annotation.IntegrationEventType;
import com.warehouse.eventoutbox.integration.model.IntegrationEvent;
import com.warehouse.shipment.api.event.snapshot.ShipmentIncidentAction;
import com.warehouse.shipment.api.event.snapshot.ShipmentIncidentData;
import com.warehouse.shipment.api.event.snapshot.ShipmentRepackagingData;

@JsonIgnoreProperties(ignoreUnknown = true)
@IntegrationEventType(value = "shipment.incident.resolved", version = 1, topic = ShipmentTopics.INCIDENT_EVENTS)
public class IncidentResolved extends IncidentChanged implements IntegrationEvent {

    private final ShipmentIncidentAction action;

    private final ShipmentRepackagingData repackaging;

    @JsonCreator
    public IncidentResolved(@JsonProperty("payload") final ShipmentIncidentData payload,
                            @JsonProperty("action") final ShipmentIncidentAction action,
                            @JsonProperty("repackaging") final ShipmentRepackagingData repackaging) {
        super(payload);
        this.action = action;
        this.repackaging = repackaging;
    }

    @JsonProperty("action")
    public ShipmentIncidentAction action() {
        return action;
    }

    @JsonProperty("repackaging")
    public ShipmentRepackagingData repackaging() {
        return repackaging;
    }
}
