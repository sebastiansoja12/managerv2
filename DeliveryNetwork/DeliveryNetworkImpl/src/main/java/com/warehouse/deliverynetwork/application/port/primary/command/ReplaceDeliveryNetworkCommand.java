package com.warehouse.deliverynetwork.application.port.primary.command;

import java.util.List;
import java.util.Objects;

public record ReplaceDeliveryNetworkCommand(List<DepartmentConnectionCommand> connections) {

    public ReplaceDeliveryNetworkCommand {
        Objects.requireNonNull(connections, "Connections cannot be null");
        connections = List.copyOf(connections);
    }
}
