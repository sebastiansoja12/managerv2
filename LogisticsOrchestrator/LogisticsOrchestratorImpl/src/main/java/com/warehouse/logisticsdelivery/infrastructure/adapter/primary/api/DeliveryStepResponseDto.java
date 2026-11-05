package com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api;

import java.time.LocalDateTime;

public record DeliveryStepResponseDto(int stepNumber,
                                      LocalDateTime attemptedAt,
                                      String outcome,
                                      String deliveryStatus,
                                      String userId,
                                      String departmentId,
                                      String supplierId,
                                      String vehicleId,
                                      String signatureId,
                                      String comment,
                                      String commentCode,
                                      String failureReason) {
}
