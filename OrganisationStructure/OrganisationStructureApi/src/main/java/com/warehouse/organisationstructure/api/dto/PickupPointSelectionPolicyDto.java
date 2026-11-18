package com.warehouse.organisationstructure.api.dto;

public record PickupPointSelectionPolicyDto(
        int maxDistanceKm,
        PickupPointPreferenceDto preference,
        int alternativeCount,
        FullPickupPointPolicyDto fullPointPolicy,
        boolean sameDepartmentOnly
) {
}
