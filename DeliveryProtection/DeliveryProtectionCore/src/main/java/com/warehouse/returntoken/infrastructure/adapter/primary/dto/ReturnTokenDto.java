package com.warehouse.returntoken.infrastructure.adapter.primary.dto;

import java.time.Instant;

public record ReturnTokenDto(String token, ShipmentIdDto shipmentId, Instant expiresAt) {
}
