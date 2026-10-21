package com.warehouse.palletmanagement.domain.vo;

import com.warehouse.commonassets.identificator.CarrierId;
import com.warehouse.commonassets.identificator.DriverId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.palletmanagement.domain.enumeration.TransportType;

import java.time.Instant;

public record PalletCarrier(CarrierId carrierId,
                            String carrierName,
                            TransportType transportType,
                            DriverId driverId,
                            String driverName,
                            String driverPhone,
                            VehicleId vehicleId,
                            String vehicleRegistrationNumber,
                            String trailerRegistrationNumber,
                            Instant assignedAt,
                            String assignedBy) {
}
