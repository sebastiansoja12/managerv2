package com.warehouse.deliverynetwork.api.dto;

public record DepartmentConnectionDto(
        DepartmentIdDto firstDepartmentId,
        DepartmentIdDto secondDepartmentId,
        boolean bidirectional) {

    public DepartmentConnectionDto(final DepartmentIdDto firstDepartmentId, final DepartmentIdDto secondDepartmentId) {
        this(firstDepartmentId, secondDepartmentId, true);
    }
}
