package com.warehouse.deliverynetwork.application.port.primary.command;

import java.util.List;
import java.util.Objects;

public record ReplaceDeliveryNetworkByCodesCommand(
        List<DepartmentImportCommand> departments,
        List<DepartmentConnectionCodeCommand> connections) {

    public ReplaceDeliveryNetworkByCodesCommand {
        Objects.requireNonNull(departments, "Departments cannot be null");
        Objects.requireNonNull(connections, "Connections cannot be null");
        departments = List.copyOf(departments);
        connections = List.copyOf(connections);
    }
}
