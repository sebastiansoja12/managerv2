package com.warehouse.driverhub.api.dto;

import java.math.BigDecimal;

public record CarrierVehicleDto(Long vehicleId,
                                String registrationNumber,
                                String vehicleType,
                                String trailerRegistrationNumber,
                                BigDecimal maxLoadKg,
                                boolean active) {
}
