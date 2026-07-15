package com.warehouse.pickuppoint.infrastructure.adapter.primary.api;

import com.warehouse.pickuppoint.api.dto.DepartmentIdDto;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointCapability;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record UpdatePickupPointApiRequest(
        @NotBlank String name,
        @NotNull PickupPointType type,
        @NotEmpty Set<PickupPointCapability> capabilities,
        @NotNull @Valid PickupPointAddressApi address,
        @NotNull @Valid DepartmentIdDto departmentId,
        @Valid PickupPointContactApi contact,
        String accessInstructions,
        @NotNull @Valid OpeningScheduleApi openingSchedule,
        @NotNull @Valid PickupPointServicePolicyApi servicePolicy,
        @Valid ExternalPointReferenceApi externalReference) implements PickupPointConfigurationApi {
}
