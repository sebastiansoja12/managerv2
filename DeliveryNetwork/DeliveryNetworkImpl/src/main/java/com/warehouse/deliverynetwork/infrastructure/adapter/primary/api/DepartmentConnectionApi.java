package com.warehouse.deliverynetwork.infrastructure.adapter.primary.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record DepartmentConnectionApi(
        @Valid @NotNull DepartmentIdApi firstDepartmentId,
        @Valid @NotNull DepartmentIdApi secondDepartmentId) {
}
