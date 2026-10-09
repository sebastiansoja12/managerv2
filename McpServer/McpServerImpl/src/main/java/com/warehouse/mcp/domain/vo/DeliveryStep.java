package com.warehouse.mcp.domain.vo;

import java.time.LocalDateTime;

import com.warehouse.commonassets.identificator.SupplierId;

public record DeliveryStep(int stepNumber,
                           LocalDateTime attemptedAt,
                           String outcome,
                           String deliveryStatus,
                           SupplierId supplierId,
                           String comment,
                           String failureReason) {
}
