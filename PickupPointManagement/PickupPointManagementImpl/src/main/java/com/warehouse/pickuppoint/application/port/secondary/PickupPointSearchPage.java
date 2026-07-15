package com.warehouse.pickuppoint.application.port.secondary;

import java.util.List;
import java.util.Objects;

public record PickupPointSearchPage(
        List<PickupPointSearchItem> items,
        long totalElements,
        int totalPages) {

    public PickupPointSearchPage {
        items = List.copyOf(Objects.requireNonNull(items, "Pickup point search items cannot be null"));
    }
}
