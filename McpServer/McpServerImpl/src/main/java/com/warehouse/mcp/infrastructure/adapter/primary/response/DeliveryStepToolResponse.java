package com.warehouse.mcp.infrastructure.adapter.primary.response;

public record DeliveryStepToolResponse(int stepNumber,
                                       String attemptedAt,
                                       String outcome,
                                       String deliveryStatus,
                                       Long supplierId,
                                       String comment,
                                       String failureReason) {
}
