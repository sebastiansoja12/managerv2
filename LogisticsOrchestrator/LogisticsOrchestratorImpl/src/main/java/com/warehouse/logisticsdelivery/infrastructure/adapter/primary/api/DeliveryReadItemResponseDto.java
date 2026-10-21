package com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api;

import java.time.LocalDateTime;

public record DeliveryReadItemResponseDto(String id,
                                          String shipmentId,
                                          String targetType,
                                          String targetId,
                                          String type,
                                          String deliveryStatus,
                                          String lifecycleStatus,
                                          String originDepartmentId,
                                          String destinationDepartmentId,
                                          String method,
                                          int stepCount,
                                          LocalDateTime createdAt,
                                          LocalDateTime deliveredAt) {
}
