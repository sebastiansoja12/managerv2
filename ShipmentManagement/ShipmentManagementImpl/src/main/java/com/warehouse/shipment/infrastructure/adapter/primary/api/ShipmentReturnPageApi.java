package com.warehouse.shipment.infrastructure.adapter.primary.api;

import java.util.List;

public record ShipmentReturnPageApi(
        List<ShipmentReturnDetailsApi> content,
        int page,
        int size,
        long totalElements,
        int totalPages) {
}
