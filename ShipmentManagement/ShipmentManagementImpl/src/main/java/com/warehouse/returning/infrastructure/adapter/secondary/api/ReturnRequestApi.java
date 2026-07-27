package com.warehouse.returning.infrastructure.adapter.secondary.api;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;

import java.util.List;

public record ReturnRequestApi(List<ReturnPackageRequestApi> requests) {

    public record ReturnPackageRequestApi(
            ShipmentId shipmentId,
            String reason,
            DepartmentId departmentId,
            UserId userId,
            ReasonCodeApi reasonCode
    ) {
    }

    public record ReasonCodeApi(String value) {
    }
}
