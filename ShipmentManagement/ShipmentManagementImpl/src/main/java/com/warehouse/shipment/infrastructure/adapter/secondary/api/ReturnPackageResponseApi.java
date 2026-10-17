package com.warehouse.shipment.infrastructure.adapter.secondary.api;


public record ReturnPackageResponseApi(
        ShipmentIdDto shipmentId,
        String reason,
        DepartmentCodeApi departmentCode,
        UserIdApi userId,
        ReasonCodeApi reasonCode
) {}
