package com.warehouse.pickuppoint.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointCapability;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointStatus;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointType;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;

public record PickupPointSnapshot(
        PickupPointId pickupPointId,
        PickupPointCode code,
        String name,
        PickupPointType type,
        PickupPointStatus status,
        Set<PickupPointCapability> capabilities,
        PickupPointAddress address,
        GeoCoordinates coordinates,
        DepartmentId departmentId,
        PickupPointContact contact,
        String accessInstructions,
        OpeningSchedule openingSchedule,
        PickupPointServicePolicy servicePolicy,
        ExternalPointReference externalReference,
        String statusReason,
        long version,
        Instant createdAt,
        Instant updatedAt) {

    public PickupPointSnapshot {
        Objects.requireNonNull(pickupPointId, "Pickup point ID cannot be null");
        Objects.requireNonNull(code, "Pickup point code cannot be null");
        Objects.requireNonNull(name, "Pickup point name cannot be null");
        Objects.requireNonNull(type, "Pickup point type cannot be null");
        Objects.requireNonNull(status, "Pickup point status cannot be null");
        capabilities = Set.copyOf(Objects.requireNonNull(capabilities, "Pickup point capabilities cannot be null"));
        Objects.requireNonNull(createdAt, "Creation timestamp cannot be null");
        Objects.requireNonNull(updatedAt, "Update timestamp cannot be null");
    }
}
