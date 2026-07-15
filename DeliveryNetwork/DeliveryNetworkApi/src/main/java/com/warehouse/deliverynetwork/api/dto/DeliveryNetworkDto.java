package com.warehouse.deliverynetwork.api.dto;

import java.util.List;
import java.util.Objects;

public record DeliveryNetworkDto(List<DepartmentConnectionDto> connections) {

    public DeliveryNetworkDto {
        Objects.requireNonNull(connections, "Connections cannot be null");
        connections = List.copyOf(connections);
    }
}
