package com.warehouse.palletmanagement.infrastructure.adapter.secondary.mapper;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PalletId;
import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.commonassets.identificator.PalletTransportId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.palletmanagement.domain.vo.PalletCarrier;
import com.warehouse.palletmanagement.infrastructure.adapter.secondary.entity.PalletTransportEntity;

public final class PalletTransportEntityMapper {

    private PalletTransportEntityMapper() {
    }

    public static PalletTransportEntity toEntity(final PalletId palletId,
                                                 final PalletTrackingNumber trackingNumber,
                                                 final PalletCarrier carrier,
                                                 final UserId userId,
                                                 final String username,
                                                 final DepartmentId departmentId) {
        return new PalletTransportEntity(PalletTransportId.generate(), palletId, trackingNumber, carrier.carrierId(),
                carrier.carrierName(), carrier.transportType(), carrier.driverId(), carrier.driverName(),
                carrier.driverPhone(), carrier.vehicleId(), carrier.vehicleRegistrationNumber(),
                carrier.trailerRegistrationNumber(), carrier.assignedAt(), userId, username, departmentId);
    }

    public static PalletCarrier toModel(final PalletTransportEntity entity) {
        return new PalletCarrier(entity.getCarrierId(), entity.getCarrierName(), entity.getTransportType(),
                entity.getDriverId(), entity.getDriverName(), entity.getDriverPhone(), entity.getVehicleId(),
                entity.getVehicleRegistrationNumber(), entity.getTrailerRegistrationNumber(), entity.getAssignedAt(),
                entity.getAssignedByUsername());
    }
}
