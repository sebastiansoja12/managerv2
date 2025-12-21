package com.warehouse.shipment.infrastructure.adapter.secondary.api;

import com.warehouse.reroute.infrastructure.adapter.secondary.api.ShipmentIdDto;

public record ReturnPackageRequestApi(
        ShipmentIdDto shipmentId,
        String reason,
        DepartmentCodeApi departmentCode,
        UserIdApi userId,
        ReasonCodeApi reasonCode
) {}
