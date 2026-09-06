package com.warehouse.shipment.api.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.commonassets.event.integration.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.integration.model.IntegrationEvent;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.api.event.snapshot.ShipmentEventData;

@IntegrationEventType(value = "shipment.derived", version = 1)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShipmentDerivedIntegrationEvent extends ShipmentChangedIntegrationEvent implements IntegrationEvent {

    private final ShipmentId sourceShipmentId;

    private final DerivationType derivationType;

    @JsonCreator
    public ShipmentDerivedIntegrationEvent(@JsonProperty("payload") final ShipmentEventData shipmentEventData,
                                           @JsonProperty("sourceShipmentId") final ShipmentId sourceShipmentId,
                                           @JsonProperty("derivationType") final DerivationType derivationType) {
        super(shipmentEventData);
        this.sourceShipmentId = sourceShipmentId;
        this.derivationType = derivationType;
    }

    @JsonProperty("sourceShipmentId")
    public ShipmentId sourceShipmentId() {
        return sourceShipmentId;
    }

    @JsonProperty("derivationType")
    public DerivationType derivationType() {
        return derivationType;
    }

    public enum DerivationType {
        RETURN,
        REDIRECT,
        SPLIT
    }
}
