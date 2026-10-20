package com.warehouse.logisticsdelivery.api.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.commonassets.event.integration.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.integration.model.IntegrationEvent;
import com.warehouse.logisticsdelivery.api.event.snapshot.DeliveryEventData;

@JsonIgnoreProperties(ignoreUnknown = true)
@IntegrationEventType(value = "delivery.modified", version = 1)
public class DeliveryModifiedIntegrationEvent extends DeliveryChangedIntegrationEvent implements IntegrationEvent {

    @JsonCreator
    public DeliveryModifiedIntegrationEvent(@JsonProperty("payload") final DeliveryEventData deliveryEventData) {
        super(deliveryEventData);
    }
}
