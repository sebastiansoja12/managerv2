package com.warehouse.palletmanagement.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.identificator.CarrierId;
import com.warehouse.commonassets.identificator.DriverId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.palletmanagement.domain.vo.PalletUpdate;
import com.warehouse.palletmanagement.domain.vo.PalletDimension;
import com.warehouse.palletmanagement.infrastructure.adapter.primary.dto.PalletUpdateRequestApi;
import com.warehouse.palletmanagement.domain.vo.PalletCarrierAssignment;
import com.warehouse.palletmanagement.domain.enumeration.PalletStatus;
import com.warehouse.palletmanagement.domain.enumeration.TransportType;
import com.warehouse.palletmanagement.infrastructure.adapter.primary.dto.PalletCarrierRequestApi;
import com.warehouse.palletmanagement.infrastructure.adapter.primary.dto.PalletStatusRequestApi;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.palletmanagement.application.port.primary.command.AddPalletShipmentsCommand;
import com.warehouse.palletmanagement.domain.vo.PalletDepartmentsChange;
import com.warehouse.commonassets.identificator.PalletId;
import com.warehouse.palletmanagement.infrastructure.adapter.primary.dto.PalletDepartmentsRequestApi;
import com.warehouse.palletmanagement.infrastructure.adapter.primary.dto.PalletShipmentsRequestApi;
import com.warehouse.palletmanagement.api.dto.PalletScanRequestDto;
import com.warehouse.palletmanagement.application.port.primary.command.CreatePalletCommand;
import com.warehouse.palletmanagement.application.port.primary.command.PalletScanCommand;
import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.palletmanagement.infrastructure.adapter.primary.dto.CreatePalletRequestApi;

public class PalletRequestMapper {

    public CreatePalletCommand map(final CreatePalletRequestApi request) {
        if (request == null || request.shipmentIds() == null) {
            throw new IllegalArgumentException("At least one shipment is required");
        }
        return new CreatePalletCommand(request.shipmentIds().stream().map(ShipmentId::new).toList());
    }

    public AddPalletShipmentsCommand map(final PalletId palletId, final PalletShipmentsRequestApi request) {
        if (request == null || request.shipmentIds() == null) {
            throw new IllegalArgumentException("At least one shipment is required");
        }
        return new AddPalletShipmentsCommand(palletId, request.shipmentIds().stream().map(ShipmentId::new).toList());
    }

    public PalletDepartmentsChange map(final PalletDepartmentsRequestApi request) {
        return new PalletDepartmentsChange(
                request.originDepartmentId() == null ? null : new DepartmentId(request.originDepartmentId()),
                request.destinationDepartmentId() == null ? null : new DepartmentId(request.destinationDepartmentId()));
    }

    public PalletScanCommand map(final PalletScanRequestDto request) {
        return new PalletScanCommand(new PalletTrackingNumber(request.trackingNumber()));
    }

    public PalletCarrierAssignment map(final PalletCarrierRequestApi request) {
        if (request == null) {
            throw new IllegalArgumentException("Carrier assignment is required");
        }
        return new PalletCarrierAssignment(CarrierId.of(request.carrierId()), transportType(request.transportType()),
                DriverId.of(request.driverId()), trim(request.driverName()), trim(request.driverPhone()),
                request.vehicleId() == null ? null : new VehicleId(request.vehicleId()),
                trim(request.vehicleRegistrationNumber()), trim(request.trailerRegistrationNumber()));
    }

    public PalletUpdate map(final PalletUpdateRequestApi request) {
        if (request == null) {
            throw new IllegalArgumentException("Pallet update is required");
        }
        final boolean anyDimension = request.length() != null || request.width() != null || request.height() != null;
        if (anyDimension && (request.length() == null || request.width() == null || request.height() == null)) {
            throw new IllegalArgumentException("All pallet dimensions are required");
        }
        return new PalletUpdate(trim(request.palletType()), trim(request.handlingPriority()), request.refrigerated(),
                anyDimension ? new PalletDimension(request.length(), request.width(), request.height()) : null,
                trim(request.sealNumber()));
    }

    public PalletStatus map(final PalletStatusRequestApi request) {
        if (request == null || request.palletStatus() == null || request.palletStatus().isBlank()) {
            throw new IllegalArgumentException("Pallet status is required");
        }
        return PalletStatus.valueOf(request.palletStatus().trim());
    }

    private static TransportType transportType(final String value) {
        return value == null || value.isBlank() ? null : TransportType.valueOf(value.trim());
    }

    private static String trim(final String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
