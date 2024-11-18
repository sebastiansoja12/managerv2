package com.warehouse.reroute.infrastructure.adapter.secondary.api;

import java.util.UUID;

public record RerouteProcessDto(UUID processId, ShipmentIdDto shipmentId) {

}
