package com.warehouse.mcp.infrastructure.adapter.primary.response;

import java.util.List;

public record DeliveryToolResponse(String deliveryId,
                                   Long shipmentId,
                                   String type,
                                   String deliveryStatus,
                                   String lifecycleStatus,
                                   String originDepartmentCode,
                                   String destinationDepartmentCode,
                                   String pickupMethod,
                                   String method,
                                   String pickupPointId,
                                   String deliveryPickupPointId,
                                   String courierCode,
                                   Long supplierId,
                                   Long vehicleId,
                                   Boolean signatureRequired,
                                   String instructions,
                                   String createdAt,
                                   String plannedAt,
                                   String deliveredAt,
                                   List<DeliveryStepToolResponse> steps) {
}
