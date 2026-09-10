package com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api;

import java.time.LocalDateTime;
import java.util.List;

public record DeliveryResponseDto(String id,
                                  String targetType,
                                  String targetId,
                                  String shipmentId,
                                  String type,
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
                                  LocalDateTime createdAt,
                                  LocalDateTime plannedAt,
                                  LocalDateTime deliveredAt,
                                  int attemptLimit,
                                  List<DeliveryStepResponseDto> steps) {
}
