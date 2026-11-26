package com.warehouse.driverhub.api.dto;

import java.math.BigDecimal;

public record CarrierVehicleCreateDto(String registrationNumber,
                                      String brand,
                                      String model,
                                      String vehicleType,
                                      BigDecimal maxLoadKg) {
}
