package com.warehouse.pickuppoint.infrastructure.adapter.primary.api;

import com.warehouse.pickuppoint.api.dto.DepartmentIdDto;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointCapability;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointType;

import java.util.Set;

public interface PickupPointConfigurationApi {

    String name();

    PickupPointType type();

    Set<PickupPointCapability> capabilities();

    PickupPointAddressApi address();

    DepartmentIdDto departmentId();

    PickupPointContactApi contact();

    String accessInstructions();

    OpeningScheduleApi openingSchedule();

    PickupPointServicePolicyApi servicePolicy();

    ExternalPointReferenceApi externalReference();
}
