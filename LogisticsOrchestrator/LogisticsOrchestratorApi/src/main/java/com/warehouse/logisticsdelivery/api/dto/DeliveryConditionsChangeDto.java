package com.warehouse.logisticsdelivery.api.dto;

import java.time.LocalDateTime;

public record DeliveryConditionsChangeDto(String method,
                                          Boolean signatureRequired,
                                          String instructions,
                                          LocalDateTime plannedAt) {
}
