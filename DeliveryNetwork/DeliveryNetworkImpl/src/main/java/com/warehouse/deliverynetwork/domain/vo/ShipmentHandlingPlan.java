package com.warehouse.deliverynetwork.domain.vo;

import java.util.List;

public record ShipmentHandlingPlan(List<RouteLeg> legs, List<HandlingOperation> operations) {

    public ShipmentHandlingPlan {
        legs = List.copyOf(legs);
        operations = List.copyOf(operations);
    }
}
