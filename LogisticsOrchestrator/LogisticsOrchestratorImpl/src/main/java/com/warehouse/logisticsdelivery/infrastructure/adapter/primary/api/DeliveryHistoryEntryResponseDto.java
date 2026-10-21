package com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api;

import java.time.Instant;
import java.time.LocalDateTime;

public record DeliveryHistoryEntryResponseDto(String eventType,
                                              Instant occurredAt,
                                              String userId,
                                              String deliveryStatus,
                                              String lifecycleStatus,
                                              String originDepartmentId,
                                              String destinationDepartmentId,
                                              String pickupMethod,
                                              String method,
                                              String pickupPointId,
                                              String deliveryPickupPointId,
                                              String supplierId,
                                              String vehicleId,
                                              Boolean signatureRequired,
                                              String instructions,
                                              LocalDateTime plannedAt,
                                              int stepCount) {
}
