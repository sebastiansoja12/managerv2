package com.warehouse.pallet.infrastructure.adapter.primary.kafka.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DeliveryChangedIntegrationEvent(String eventId, String eventType, DeliveryEventData payload) {
}
