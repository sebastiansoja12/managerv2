package com.warehouse.palletmanagement.infrastructure.adapter.primary.dto;

import java.time.Instant;

public record PalletCarrierResponseApi(Long carrierId,
                                       String carrierName,
                                       String transportType,
                                       Long driverId,
                                       String driverName,
                                       String driverPhone,
                                       Long vehicleId,
                                       String vehicleRegistrationNumber,
                                       String trailerRegistrationNumber,
                                       Instant assignedAt,
                                       String assignedBy) {
}
