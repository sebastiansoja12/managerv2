package com.warehouse.returning.infrastructure.adapter.primary.kafka.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.warehouse.returning.domain.vo.ShipmentId;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ShipmentSnapshot(ShipmentId shipmentId, String shipmentStatus) {
}
