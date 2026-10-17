package com.warehouse.logistics.infrastructure.adapter.primary.dto;

import java.time.LocalDateTime;
import java.util.List;

public record DeliveryResponseDto(String id,
                                  String targetType,
                                  String targetId,
                                  String shipmentId,
                                  String type,
                                  String deliveryStatus,
                                  String lifecycleStatus,
                                  String method,
                                  LocalDateTime createdAt,
                                  LocalDateTime plannedAt,
                                  LocalDateTime deliveredAt,
                                  List<DeliveryStepResponseDto> steps) {
}
