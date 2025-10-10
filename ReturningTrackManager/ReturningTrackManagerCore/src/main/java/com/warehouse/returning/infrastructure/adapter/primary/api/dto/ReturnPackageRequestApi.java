package com.warehouse.returning.infrastructure.adapter.primary.api.dto;

import lombok.Builder;

@Builder
public record ReturnPackageRequestApi(
        ShipmentIdApi shipmentId,
        String reason,
        DepartmentCodeApi departmentCode,
        UserIdApi userId,
        ReasonCodeApi reasonCode
) {}