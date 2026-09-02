package com.warehouse.deliverynetwork.application.port.primary.result;

import java.util.List;
import java.util.Objects;

public record DeliveryNetworkExportResult(
        List<DepartmentExportResult> departments,
        List<DepartmentConnectionCodeResult> connections) {

    public DeliveryNetworkExportResult {
        Objects.requireNonNull(departments, "Departments cannot be null");
        Objects.requireNonNull(connections, "Connections cannot be null");
        departments = List.copyOf(departments);
        connections = List.copyOf(connections);
    }
}
