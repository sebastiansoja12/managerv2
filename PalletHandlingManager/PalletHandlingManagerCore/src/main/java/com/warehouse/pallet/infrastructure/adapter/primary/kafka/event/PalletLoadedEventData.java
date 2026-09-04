package com.warehouse.pallet.infrastructure.adapter.primary.kafka.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PalletLoadedEventData(String palletId, String trackingNumber, List<Long> shipmentIds) {
}
