package com.warehouse.pickuppoint.application.port.primary.command;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointCapability;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointType;
import com.warehouse.pickuppoint.domain.vo.ExternalPointReference;
import com.warehouse.pickuppoint.domain.vo.OpeningSchedule;
import com.warehouse.pickuppoint.domain.vo.PickupPointAddress;
import com.warehouse.pickuppoint.domain.vo.PickupPointContact;
import com.warehouse.pickuppoint.domain.vo.PickupPointServicePolicy;

import java.util.Objects;
import java.util.Set;

public record PickupPointConfigurationCommand(
        String name,
        PickupPointType type,
        Set<PickupPointCapability> capabilities,
        PickupPointAddress address,
        DepartmentId departmentId,
        PickupPointContact contact,
        String accessInstructions,
        OpeningSchedule openingSchedule,
        PickupPointServicePolicy servicePolicy,
        ExternalPointReference externalReference) {

    public PickupPointConfigurationCommand {
        Objects.requireNonNull(name, "Pickup point name cannot be null");
        Objects.requireNonNull(type, "Pickup point type cannot be null");
        capabilities = Set.copyOf(Objects.requireNonNull(capabilities, "Pickup point capabilities cannot be null"));
    }
}
