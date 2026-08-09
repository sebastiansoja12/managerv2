package com.warehouse.shipment.infrastructure.adapter.primary.api;

import java.math.BigDecimal;

public record DimensionsApi(BigDecimal length, BigDecimal width, BigDecimal height, LengthUnitDto unit) {
}
