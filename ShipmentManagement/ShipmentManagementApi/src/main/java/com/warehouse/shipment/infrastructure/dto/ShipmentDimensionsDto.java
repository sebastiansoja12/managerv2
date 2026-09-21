package com.warehouse.shipment.infrastructure.dto;

import java.math.BigDecimal;

public record ShipmentDimensionsDto(BigDecimal length,
                                    BigDecimal width,
                                    BigDecimal height,
                                    String unit) {
}
