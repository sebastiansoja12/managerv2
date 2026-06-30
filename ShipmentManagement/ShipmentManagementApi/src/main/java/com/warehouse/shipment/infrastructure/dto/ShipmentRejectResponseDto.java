package com.warehouse.shipment.infrastructure.dto;

import java.util.List;

public record ShipmentRejectResponseDto(List<ShipmentRejectResponseItemDto> shipments) {
}
