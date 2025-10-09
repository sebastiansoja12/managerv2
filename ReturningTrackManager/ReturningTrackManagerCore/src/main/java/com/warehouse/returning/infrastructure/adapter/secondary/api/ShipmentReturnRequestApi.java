package com.warehouse.returning.infrastructure.adapter.secondary.api;

public record ShipmentReturnRequestApi(ShipmentIdApi shipmentId, String reason, DepartmentCodeApi departmentCode,
                                       UserIdApi issuedBy) {
}
