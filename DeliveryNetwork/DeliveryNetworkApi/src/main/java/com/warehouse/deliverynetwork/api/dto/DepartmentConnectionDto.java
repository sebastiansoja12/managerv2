package com.warehouse.deliverynetwork.api.dto;

public record DepartmentConnectionDto(
        DepartmentIdDto firstDepartmentId,
        DepartmentIdDto secondDepartmentId) {
}
