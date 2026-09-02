package com.warehouse.deliverynetwork.application.port.primary.result;

import com.warehouse.deliverynetwork.domain.vo.DepartmentConnection;

import java.util.Objects;
import java.util.Set;

public record DeliveryNetworkResult(Set<DepartmentConnection> connections) {

    public DeliveryNetworkResult {
        Objects.requireNonNull(connections, "Connections cannot be null");
        connections = Set.copyOf(connections);
    }
}
