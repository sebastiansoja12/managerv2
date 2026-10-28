package com.warehouse.shipment.application.port.primary.result;

import java.util.List;

public record ShipmentPageResult(
        List<ShipmentResult> content,
        int page,
        int size,
        long totalElements,
        int totalPages) {
}
