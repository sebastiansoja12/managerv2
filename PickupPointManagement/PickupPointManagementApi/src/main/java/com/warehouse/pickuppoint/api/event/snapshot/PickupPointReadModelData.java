package com.warehouse.pickuppoint.api.event.snapshot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.warehouse.pickuppoint.api.dto.PickupPointIdDto;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PickupPointReadModelData(PickupPointIdDto pickupPointId) {

    public PickupPointReadModelData {
        Objects.requireNonNull(pickupPointId, "Pickup point ID cannot be null");
    }
}
