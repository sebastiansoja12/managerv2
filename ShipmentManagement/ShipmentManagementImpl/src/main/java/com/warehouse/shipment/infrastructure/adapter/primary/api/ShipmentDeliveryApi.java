package com.warehouse.shipment.infrastructure.adapter.primary.api;

import java.time.LocalDateTime;

public record ShipmentDeliveryApi(DepartmentIdDto originDepartmentId,
                                  DepartmentIdDto destinationDepartmentId,
                                  LocalDateTime plannedDeliveryAt,
                                  LocalDateTime deliveredAt) {
}
