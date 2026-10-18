package com.warehouse.palletmanagement.domain.vo;

import com.warehouse.commonassets.identificator.CarrierId;
import com.warehouse.commonassets.identificator.DriverId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.palletmanagement.domain.enumeration.TransportType;

public record PalletCarrierAssignment(CarrierId carrierId,
                                      TransportType transportType,
                                      DriverId driverId,
                                      String driverName,
                                      String driverPhone,
                                      VehicleId vehicleId,
                                      String vehicleRegistrationNumber,
                                      String trailerRegistrationNumber) {

    public PalletCarrierAssignment {
        if (carrierId == null || carrierId.getValue() == null) {
            throw new IllegalArgumentException("Carrier is required");
        }
    }
}
