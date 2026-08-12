package com.warehouse.routetracker.infrastructure.adapter.primary.dto;

public record DepartmentIdRequestDto(DepartmentIdDto departmentId, ShipmentIdDto shipmentId,
                                     ProcessTypeDto processType) {
}
