package com.warehouse.shipment.api.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.commonassets.event.integration.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.integration.context.OperatorAwareContext;
import com.warehouse.commonassets.event.integration.model.IntegrationEvent;
import com.warehouse.commonassets.event.integration.model.IntegrationEventKey;
import com.warehouse.shipment.api.event.snapshot.ShipmentDeliveryReadModelData;

@JsonIgnoreProperties(ignoreUnknown = true)
@IntegrationEventType(value = "shipment.read-model.delivery-changed", version = 1)
public final class ShipmentDeliveryReadModelChanged extends OperatorAwareContext
        implements IntegrationEvent, IntegrationEventKey {

    private final ShipmentDeliveryReadModelData payload;

    @JsonCreator
    public ShipmentDeliveryReadModelChanged(@JsonProperty("payload") final ShipmentDeliveryReadModelData payload) {
        this.payload = payload;
    }

    @JsonProperty("payload")
    public ShipmentDeliveryReadModelData payload() {
        return payload;
    }

    @Override
    public String eventKey() {
        return String.valueOf(this.payload.shipmentId().getValue());
    }
}
