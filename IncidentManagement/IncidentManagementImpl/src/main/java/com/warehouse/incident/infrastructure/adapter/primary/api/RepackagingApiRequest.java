package com.warehouse.incident.infrastructure.adapter.primary.api;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record RepackagingApiRequest(String packagingType,
                                    @Positive BigDecimal weightInKilograms,
                                    @Positive BigDecimal lengthInCentimeters,
                                    @Positive BigDecimal widthInCentimeters,
                                    @Positive BigDecimal heightInCentimeters) {
}
