package com.warehouse.shipment.infrastructure.adapter.primary.api;

import java.util.List;

public record ShipmentPageDto(
        List<ShipmentDto> content,
        int page,
        int size,
        long totalElements,
        int totalPages) {
}
