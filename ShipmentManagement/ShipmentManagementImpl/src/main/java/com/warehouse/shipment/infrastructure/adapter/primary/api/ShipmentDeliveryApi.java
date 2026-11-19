package com.warehouse.shipment.infrastructure.adapter.primary.api;

import java.time.LocalDateTime;

public record ShipmentDeliveryApi(DepartmentIdDto originDepartmentId,
                                  DepartmentIdDto destinationDepartmentId,
                                  DepartmentIdDto currentDepartmentId,
                                  LocalDateTime plannedDeliveryAt,
                                  LocalDateTime deliveredAt) {
}
