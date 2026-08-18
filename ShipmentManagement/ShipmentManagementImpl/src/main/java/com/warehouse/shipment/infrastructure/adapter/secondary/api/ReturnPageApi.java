package com.warehouse.shipment.infrastructure.adapter.secondary.api;

import java.util.List;

public record ReturnPageApi(
        List<ReturnPackageApi> content,
        int page,
        int size,
        long totalElements,
        int totalPages) {
}
