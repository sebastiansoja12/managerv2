package com.warehouse.shipment.infrastructure.dto;

import java.util.List;

public record ShipmentRejectRequestDto(List<ShipmentRejectRequestItemDto> shipments) {
}
