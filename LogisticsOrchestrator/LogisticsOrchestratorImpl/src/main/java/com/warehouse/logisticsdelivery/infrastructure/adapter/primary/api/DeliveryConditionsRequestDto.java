package com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api;

import java.time.LocalDateTime;

public record DeliveryConditionsRequestDto(String method,
                                           Boolean signatureRequired,
                                           String instructions,
                                           LocalDateTime plannedAt) {
}
