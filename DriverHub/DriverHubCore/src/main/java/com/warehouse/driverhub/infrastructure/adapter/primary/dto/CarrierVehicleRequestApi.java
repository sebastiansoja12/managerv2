package com.warehouse.driverhub.infrastructure.adapter.primary.dto;

import java.math.BigDecimal;

public record CarrierVehicleRequestApi(String registrationNumber, String brand, String model, String vehicleType,
                                       String trailerRegistrationNumber, BigDecimal maxLoadKg) {
}
