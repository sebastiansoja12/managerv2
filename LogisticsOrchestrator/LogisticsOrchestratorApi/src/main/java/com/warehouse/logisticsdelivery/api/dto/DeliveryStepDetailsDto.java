package com.warehouse.logisticsdelivery.api.dto;

import java.time.LocalDateTime;

import com.warehouse.commonassets.identificator.SupplierId;

public record DeliveryStepDetailsDto(int stepNumber,
                                     LocalDateTime attemptedAt,
                                     String outcome,
                                     String deliveryStatus,
                                     SupplierId supplierId,
                                     String comment,
                                     String failureReason) {
}
