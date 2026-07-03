package com.warehouse.pickuppoint.infrastructure.adapter.primary.api;

import com.warehouse.pickuppoint.domain.enumeration.PickupPointShipmentSize;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record PickupPointServicePolicyApi(
        @NotEmpty Set<PickupPointShipmentSize> allowedShipmentSizes,
        boolean acceptsDangerousGoods) {
}
