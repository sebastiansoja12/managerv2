package com.warehouse.shipment.api.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.eventoutbox.integration.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.integration.context.OperatorAwareContext;
import com.warehouse.eventoutbox.integration.model.IntegrationEvent;
import com.warehouse.eventoutbox.integration.model.IntegrationEventKey;
import com.warehouse.shipment.api.event.snapshot.ShipmentPalletReadModelData;

@JsonIgnoreProperties(ignoreUnknown = true)
@IntegrationEventType(value = "shipment.read-model.pallet-changed", version = 1, topic = ShipmentTopics.PALLET_READ_MODEL_SYNC)
public final class ShipmentPalletReadModelChanged extends OperatorAwareContext
        implements IntegrationEvent, IntegrationEventKey {

    private final ShipmentPalletReadModelData payload;

    @JsonCreator
    public ShipmentPalletReadModelChanged(@JsonProperty("payload") final ShipmentPalletReadModelData payload) {
        this.payload = payload;
    }

    @JsonProperty("payload")
    public ShipmentPalletReadModelData payload() {
        return payload;
    }

    @Override
    public String eventKey() {
        return String.valueOf(this.payload.shipmentId().getValue());
    }
}
