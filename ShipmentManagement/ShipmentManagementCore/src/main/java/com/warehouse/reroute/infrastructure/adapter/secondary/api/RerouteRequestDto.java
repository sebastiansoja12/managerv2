package com.warehouse.reroute.infrastructure.adapter.secondary.api;

public record RerouteRequestDto(ShipmentIdDto shipmentId, String shipmentStatus) {
}
