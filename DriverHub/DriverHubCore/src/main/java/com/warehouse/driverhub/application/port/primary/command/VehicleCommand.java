package com.warehouse.driverhub.application.port.primary.command;

import com.warehouse.driverhub.domain.enumeration.VehicleType;

import java.math.BigDecimal;

public record VehicleCommand(String registrationNumber, String brand, String model, VehicleType vehicleType,
                             String trailerRegistrationNumber, BigDecimal maxLoadKg) {
}
