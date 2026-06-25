package com.warehouse.pickuppoint.api.dto;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;

public record PickupPointSelectionDto(
        PickupPointIdDto pickupPointId,
        String code,
        String name,
        PickupPointTypeDto type,
        PickupPointStatusDto status,
        Set<PickupPointCapabilityDto> capabilities,
        PickupPointAddressDto address,
        GeoCoordinatesDto coordinates,
        DepartmentIdDto departmentId,
        long version,
        Instant evaluatedAt) {

    public PickupPointSelectionDto {
        Objects.requireNonNull(pickupPointId, "Pickup point ID cannot be null");
        Objects.requireNonNull(code, "Pickup point code cannot be null");
        Objects.requireNonNull(name, "Pickup point name cannot be null");
        Objects.requireNonNull(type, "Pickup point type cannot be null");
        Objects.requireNonNull(status, "Pickup point status cannot be null");
        capabilities = Set.copyOf(Objects.requireNonNull(capabilities, "Pickup point capabilities cannot be null"));
        Objects.requireNonNull(evaluatedAt, "Evaluation timestamp cannot be null");
    }
}
