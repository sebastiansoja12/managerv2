package com.warehouse.pickuppoint.api.dto;

import java.util.Objects;
import java.util.UUID;

public record PickupPointIdDto(UUID value) {

    public PickupPointIdDto {
        Objects.requireNonNull(value, "Pickup point ID value cannot be null");
    }
}
