package com.warehouse.supplier.infrastructure.adapter.primary.mapper;

import java.util.Set;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.supplier.api.dto.CreateSupplierApiRequest;
import com.warehouse.supplier.api.dto.DeliveryAreaCourierDto;
import com.warehouse.supplier.api.dto.SupplierCodeDto;
import com.warehouse.supplier.api.dto.SupplierDeliveryAreaDto;
import com.warehouse.supplier.api.dto.SupplierDetailsDto;
import com.warehouse.supplier.api.dto.SupplierProfileDto;
import com.warehouse.supplier.application.port.primary.command.ChangeSupplierDeliveryAreaCommand;
import com.warehouse.supplier.application.port.primary.command.ChangeSupplierVehicleCommand;
import com.warehouse.supplier.application.port.primary.command.SupplierBasicDataUpdateCommand;
import com.warehouse.supplier.application.port.primary.command.SupplierCreateCommand;
import com.warehouse.supplier.application.port.primary.result.SupplierCreateResponse;
import com.warehouse.supplier.application.port.primary.result.SupplierResult;
import com.warehouse.supplier.domain.enumeration.SupplierStatus;
import com.warehouse.supplier.domain.vo.DeliveryArea;
import com.warehouse.supplier.domain.vo.SupplierSnapshot;

public abstract class SupplierApiServiceMapper {

    public static SupplierCreateCommand map(final CreateSupplierApiRequest request) {
        return new SupplierCreateCommand(
                request.supplierCode() != null ? new SupplierCode(request.supplierCode().value()) : null,
                request.firstName(),
                request.lastName(),
                request.telephoneNumber(),
                request.email(),
                new DepartmentCode(request.departmentCode()));
    }

    public static SupplierBasicDataUpdateCommand map(final SupplierCode supplierCode, final String firstName,
                                                     final String lastName, final String telephoneNumber) {
        return new SupplierBasicDataUpdateCommand(supplierCode, firstName, lastName, telephoneNumber);
    }

    public static ChangeSupplierVehicleCommand map(final SupplierCode supplierCode, final VehicleId vehicleId) {
        return new ChangeSupplierVehicleCommand(supplierCode, vehicleId);
    }

    public static ChangeSupplierDeliveryAreaCommand map(final SupplierCode supplierCode,
                                                        final SupplierDeliveryAreaDto deliveryArea) {
        return new ChangeSupplierDeliveryAreaCommand(supplierCode, new DeliveryArea(deliveryArea.areaName(),
                deliveryArea.city(), deliveryArea.district(), deliveryArea.municipality(), deliveryArea.region(),
                deliveryArea.country(), deliveryArea.postalCodes()));
    }

    public static SupplierCodeDto map(final SupplierCreateResponse response) {
        return new SupplierCodeDto(response.supplierCode().value());
    }

    public static SupplierDetailsDto mapDetails(final SupplierResult result) {
        final SupplierSnapshot supplier = result.supplier();
        final DepartmentCode departmentCode = result.departmentCode();
        return new SupplierDetailsDto(
                supplier.supplierId() != null ? supplier.supplierId().value() : null,
                new SupplierCodeDto(supplier.supplierCode().value()),
                supplier.firstName(),
                supplier.lastName(),
                supplier.telephoneNumber(),
                departmentCode != null ? departmentCode.getValue() : null,
                supplier.status() != null ? supplier.status().name() : null);
    }

    public static DeliveryAreaCourierDto mapDeliveryAreaCourier(final SupplierSnapshot supplier) {
        return new DeliveryAreaCourierDto(supplier.supplierId(), supplier.vehicleId(),
                Set.copyOf(supplier.deliveryArea().postalCodes()));
    }

    public static SupplierProfileDto mapProfile(final SupplierResult result) {
        final SupplierSnapshot supplier = result.supplier();
        return new SupplierProfileDto(
                supplier.supplierId(),
                supplier.supplierCode(),
                supplier.firstName(),
                supplier.lastName(),
                supplier.departmentId(),
                supplier.status() != null ? supplier.status().name() : null,
                supplier.status() == SupplierStatus.ACTIVE,
                supplier.vehicleId());
    }
}
