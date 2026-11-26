package com.warehouse.mcp.domain.vo;

import java.math.BigDecimal;

import com.warehouse.commonassets.identificator.CarrierId;

public record VehicleDraft(CarrierId carrierId,
                           String registrationNumber,
                           String brand,
                           String model,
                           String vehicleType,
                           BigDecimal maxLoadKg) {
}
