package com.warehouse.pickuppoint.infrastructure.adapter.primary.api;

import com.warehouse.pickuppoint.api.dto.PickupPointIdDto;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointCapability;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointStatus;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointType;

import java.time.Instant;
import java.util.Set;

public record PickupPointApiResponse(
        PickupPointIdDto pickupPointId,
        String code,
        String name,
        PickupPointType type,
        PickupPointStatus status,
        Set<PickupPointCapability> capabilities,
        PickupPointAddressApi address,
        GeoCoordinatesApi coordinates,
        PickupPointDepartmentApi department,
        PickupPointAvailabilityApi availability,
        long version,
        PickupPointContactApi contact,
        String accessInstructions,
        OpeningScheduleApi openingSchedule,
        PickupPointServicePolicyApi servicePolicy,
        ExternalPointReferenceApi externalReference,
        String statusReason,
        Instant createdAt,
        Instant updatedAt) {
}
