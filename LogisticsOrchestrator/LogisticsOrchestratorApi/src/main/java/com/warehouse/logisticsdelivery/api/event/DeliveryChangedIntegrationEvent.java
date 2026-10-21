package com.warehouse.logisticsdelivery.api.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.commonassets.event.integration.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.integration.context.OperatorAwareContext;
import com.warehouse.commonassets.event.integration.model.IntegrationEvent;
import com.warehouse.commonassets.event.integration.model.IntegrationEventKey;
import com.warehouse.logisticsdelivery.api.event.snapshot.DeliveryEventData;

@JsonIgnoreProperties(ignoreUnknown = true)
@IntegrationEventType(value = "delivery.changed", version = 1)
public class DeliveryChangedIntegrationEvent extends OperatorAwareContext
        implements IntegrationEvent, IntegrationEventKey {

    private final DeliveryEventData payload;

    @JsonCreator
    public DeliveryChangedIntegrationEvent(@JsonProperty("payload") final DeliveryEventData deliveryEventData) {
        this.payload = deliveryEventData;
    }

    @JsonProperty("payload")
    public DeliveryEventData payload() {
        return payload;
    }

    @Override
    public String eventKey() {
        return this.payload.deliveryId().getId();
    }
}
