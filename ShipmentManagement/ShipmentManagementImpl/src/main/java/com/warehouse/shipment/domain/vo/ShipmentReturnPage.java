package com.warehouse.shipment.domain.vo;

import java.util.List;

public record ShipmentReturnPage(
        List<ShipmentReturnDetails> content,
        int page,
        int size,
        long totalElements,
        int totalPages) {
}
