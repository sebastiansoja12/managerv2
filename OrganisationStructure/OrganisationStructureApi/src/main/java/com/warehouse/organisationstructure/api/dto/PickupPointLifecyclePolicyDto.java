package com.warehouse.organisationstructure.api.dto;

public record PickupPointLifecyclePolicyDto(
        boolean requireStatusChangeReason,
        SuspendedPickupPointShipmentPolicyDto suspendedPointShipmentPolicy,
        boolean blockClosingWithPendingShipments,
        int autoResumeSuspendedAfterDays
) {
}
