package com.warehouse.organisationstructure.api.dto;

public record PickupPointCapacityPolicyDto(
        int defaultCapacity,
        int capacityWarningThresholdPercent,
        boolean blockIntakeWhenFull
) {
}
