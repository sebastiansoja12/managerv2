package com.warehouse.shipment.infrastructure.adapter.secondary.kafka.event;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.warehouse.commonassets.kafka.infrastructure.annotation.KafkaTopic;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

@JsonIgnoreProperties(ignoreUnknown = true)
@KafkaTopic("${manager.kafka.topics.shipment-read-model-sync:shipment.read-model.sync}")
public record ShipmentReadModelChanged(ShipmentSnapshot snapshot,
                                       Instant timestamp) {
}
