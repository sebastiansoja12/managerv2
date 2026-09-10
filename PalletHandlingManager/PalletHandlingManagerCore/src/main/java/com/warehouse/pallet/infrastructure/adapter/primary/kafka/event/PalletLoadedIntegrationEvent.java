package com.warehouse.pallet.infrastructure.adapter.primary.kafka.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PalletLoadedIntegrationEvent(String eventId, PalletLoadedEventData payload) {
}
