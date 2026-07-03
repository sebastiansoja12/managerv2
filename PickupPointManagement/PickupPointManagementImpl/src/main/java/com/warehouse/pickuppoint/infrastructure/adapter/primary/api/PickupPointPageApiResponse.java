package com.warehouse.pickuppoint.infrastructure.adapter.primary.api;

import java.time.Instant;
import java.util.List;

public record PickupPointPageApiResponse(
        List<PickupPointApiResponse> items,
        int page,
        int size,
        long totalElements,
        int totalPages,
        Instant evaluatedAt) {
}
