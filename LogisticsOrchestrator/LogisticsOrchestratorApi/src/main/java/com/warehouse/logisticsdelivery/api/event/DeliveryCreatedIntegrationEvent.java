package com.warehouse.logisticsdelivery.api.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.eventoutbox.integration.annotation.IntegrationEventType;
import com.warehouse.eventoutbox.integration.model.IntegrationEvent;
import com.warehouse.logisticsdelivery.api.event.snapshot.DeliveryEventData;

@JsonIgnoreProperties(ignoreUnknown = true)
@IntegrationEventType(value = "delivery.created", version = 1, topic = DeliveryTopics.EVENTS)
public class DeliveryCreatedIntegrationEvent extends DeliveryChangedIntegrationEvent implements IntegrationEvent {

    @JsonCreator
    public DeliveryCreatedIntegrationEvent(@JsonProperty("payload") final DeliveryEventData deliveryEventData) {
        super(deliveryEventData);
    }
}
