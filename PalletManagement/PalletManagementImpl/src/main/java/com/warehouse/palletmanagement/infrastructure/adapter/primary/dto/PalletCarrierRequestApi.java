package com.warehouse.palletmanagement.infrastructure.adapter.primary.dto;

public record PalletCarrierRequestApi(Long carrierId,
                                      String transportType,
                                      Long driverId,
                                      String driverName,
                                      String driverPhone,
                                      Long vehicleId,
                                      String vehicleRegistrationNumber,
                                      String trailerRegistrationNumber) {
}
