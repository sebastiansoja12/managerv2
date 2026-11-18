package com.warehouse.organisationstructure.api.dto;

public record PickupPointLockerPolicyDto(
        int courierCompartmentHoldMinutes,
        int compartmentReservationHours,
        OversizedLockerShipmentPolicyDto oversizedShipmentPolicy,
        int suspendAfterCompartmentFailures
) {
}
