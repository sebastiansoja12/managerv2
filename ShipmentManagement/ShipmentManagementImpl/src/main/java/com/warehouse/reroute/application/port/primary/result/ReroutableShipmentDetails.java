package com.warehouse.reroute.application.port.primary.result;

import java.time.Instant;

import com.warehouse.reroute.domain.vo.ReroutableShipment;

public record ReroutableShipmentDetails(ReroutableShipment shipment, Instant tokenExpiresAt) {
}
