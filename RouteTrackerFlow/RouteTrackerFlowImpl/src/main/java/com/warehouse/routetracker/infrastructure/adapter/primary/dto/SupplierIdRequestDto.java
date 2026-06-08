package com.warehouse.routetracker.infrastructure.adapter.primary.dto;

public record SupplierIdRequestDto(SupplierIdDto supplierId, ShipmentIdDto shipmentId,
                                   ProcessTypeDto processType) {
}
