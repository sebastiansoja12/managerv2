package com.warehouse.organisationstructure.api.dto;

public record PickupPointConfigurationDto(
        PickupPointDefaultsPolicyDto defaultsPolicy,
        PickupPointCollectionPolicyDto collectionPolicy,
        PickupPointLockerPolicyDto lockerPolicy,
        PickupPointSelectionPolicyDto selectionPolicy,
        PickupPointCapacityPolicyDto capacityPolicy,
        PickupPointLifecyclePolicyDto lifecyclePolicy
) {
}
