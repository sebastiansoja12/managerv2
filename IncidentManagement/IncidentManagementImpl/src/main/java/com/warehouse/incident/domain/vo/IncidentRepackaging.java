package com.warehouse.incident.domain.vo;

import java.math.BigDecimal;

public record IncidentRepackaging(String packagingType,
                                  BigDecimal weightInKilograms,
                                  BigDecimal lengthInCentimeters,
                                  BigDecimal widthInCentimeters,
                                  BigDecimal heightInCentimeters) {
}
