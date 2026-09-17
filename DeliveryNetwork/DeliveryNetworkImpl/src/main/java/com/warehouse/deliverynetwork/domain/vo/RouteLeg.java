package com.warehouse.deliverynetwork.domain.vo;

public record RouteLeg(
        int sequence,
        DepartmentNode originDepartment,
        DepartmentNode destinationDepartment,
        Double distanceInKilometers) {
}
