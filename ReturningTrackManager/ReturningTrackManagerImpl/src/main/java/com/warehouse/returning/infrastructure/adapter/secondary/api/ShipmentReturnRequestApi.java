package com.warehouse.returning.infrastructure.adapter.secondary.api;

import com.warehouse.common.DepartmentId;
public record ShipmentReturnRequestApi(ShipmentIdApi shipmentId, String reason, DepartmentId departmentId,
                                       UserIdApi issuedBy, String returnStatus) {
}
