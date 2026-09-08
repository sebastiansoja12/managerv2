package com.warehouse.logisticsdelivery.api.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.commonassets.event.integration.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.integration.model.IntegrationEvent;
import com.warehouse.logisticsdelivery.api.event.snapshot.DeliveryEventData;

@JsonIgnoreProperties(ignoreUnknown = true)
@IntegrationEventType(value = "delivery.completed", version = 1)
public class DeliveryCompletedIntegrationEvent extends DeliveryChangedIntegrationEvent implements IntegrationEvent {

    @JsonCreator
    public DeliveryCompletedIntegrationEvent(@JsonProperty("payload") final DeliveryEventData deliveryEventData) {
        super(deliveryEventData);
    }
}
