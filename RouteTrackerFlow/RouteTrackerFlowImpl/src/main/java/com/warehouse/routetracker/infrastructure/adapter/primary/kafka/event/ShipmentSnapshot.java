package com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.ShipmentId;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ShipmentSnapshot(ShipmentId shipmentId, String shipmentStatus) {
}
