package com.warehouse.pickuppoint.application.port.primary.result;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public record PickupPointPageResult(
        List<PickupPointListItemResult> items,
        int page,
        int size,
        long totalElements,
        int totalPages,
        Instant evaluatedAt) {

    public PickupPointPageResult {
        items = List.copyOf(Objects.requireNonNull(items, "Pickup point page items cannot be null"));
        Objects.requireNonNull(evaluatedAt, "Evaluation timestamp cannot be null");
    }
}
