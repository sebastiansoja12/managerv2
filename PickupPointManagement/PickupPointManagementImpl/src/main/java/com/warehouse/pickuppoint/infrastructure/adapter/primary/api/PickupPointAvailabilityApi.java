package com.warehouse.pickuppoint.infrastructure.adapter.primary.api;

import java.util.List;

public record PickupPointAvailabilityApi(
        boolean selectable,
        List<String> reasonCodes,
        Boolean isOpenNow) {
}
