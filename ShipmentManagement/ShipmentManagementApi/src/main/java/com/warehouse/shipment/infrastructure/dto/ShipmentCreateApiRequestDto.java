package com.warehouse.shipment.infrastructure.dto;

import java.math.BigDecimal;

public record ShipmentCreateApiRequestDto(ShipmentPartyRequestDto sender,
                                          ShipmentPartyRequestDto recipient,
                                          BigDecimal lengthCm,
                                          BigDecimal widthCm,
                                          BigDecimal heightCm,
                                          BigDecimal weightKg,
                                          String contentDescription,
                                          BigDecimal price,
                                          BigDecimal declaredValue,
                                          String currency,
                                          String customerReference,
                                          String priority,
                                          String packagingType,
                                          String serviceLevel,
                                          String pickupMethod,
                                          String deliveryMethod,
                                          String pickupPointId,
                                          String deliveryPickupPointId) {
}
