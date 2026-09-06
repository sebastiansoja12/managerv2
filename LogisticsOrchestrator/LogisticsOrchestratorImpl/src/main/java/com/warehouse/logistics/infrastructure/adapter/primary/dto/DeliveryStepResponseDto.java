package com.warehouse.logistics.infrastructure.adapter.primary.dto;

import java.time.LocalDateTime;

public record DeliveryStepResponseDto(int stepNumber,
                                      LocalDateTime attemptedAt,
                                      String outcome,
                                      String deliveryStatus,
                                      String userId,
                                      String departmentId,
                                      String supplierId,
                                      String vehicleId,
                                      String comment,
                                      String failureReason) {
}
