package com.warehouse.mcp.domain.vo;

import java.math.BigDecimal;

public record ShipmentDraft(ShipmentParty sender,
                            ShipmentParty recipient,
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
