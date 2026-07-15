package com.warehouse.deliverynetwork.domain.vo;

import com.warehouse.commonassets.identificator.OperatorId;

import java.util.Objects;
import java.util.Set;

public record DeliveryNetworkSnapshot(
        OperatorId operatorId,
        Set<DepartmentConnection> connections) {

    public DeliveryNetworkSnapshot {
        Objects.requireNonNull(operatorId, "Operator ID cannot be null");
        Objects.requireNonNull(connections, "Connections cannot be null");
        connections = Set.copyOf(connections);
    }
}
