package com.warehouse.pickuppoint.infrastructure.adapter.primary.api;

import com.warehouse.pickuppoint.domain.enumeration.PickupPointStatus;
import jakarta.validation.constraints.NotNull;

public record ChangePickupPointStatusApiRequest(
        @NotNull PickupPointStatus status,
        String reason) {
}
