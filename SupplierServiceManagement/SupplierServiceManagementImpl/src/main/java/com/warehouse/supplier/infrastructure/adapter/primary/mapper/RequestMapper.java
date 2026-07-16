package com.warehouse.supplier.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.enumeration.PackageType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.supplier.domain.model.DeliveryArea;
import com.warehouse.supplier.domain.vo.*;
import com.warehouse.supplier.infrastructure.adapter.primary.dto.*;

import java.util.Set;
import java.util.stream.Collectors;

public abstract class RequestMapper {

    public static SupplierCreateCommand map(final SupplierCreateApiRequest request) {
        return new SupplierCreateCommand(request.supplierCode(), request.firstName(),
                request.lastName(), request.telephoneNumber());
    }

    public static DriverLicenseCommand map(final DriverLicenseApiRequest request) {
        return new DriverLicenseCommand(request.supplierCode(), map(request.driverLicense()));
    }

    public static DriverLicense map(final DriverLicenseApi driverLicense) {
        return new DriverLicense(driverLicense.number(), driverLicense.acquiredDate(), driverLicense.drivingLicenseExpiryDate());
    }

    public static ChangeSupportedPackageTypeCommand map(final ChangeSupportedPackageTypesApiRequest request) {
        return new ChangeSupportedPackageTypeCommand(new SupplierCode(request.supplierCode().value()),
                request.supportedPackageTypes().stream()
                        .map(packageTypeApi -> PackageType.valueOf(packageTypeApi.name()))
                        .collect(Collectors.toSet()));
    }

    public static ChangeSupplierDeviceCommand map(final ChangeSupplierDeviceApiRequest request) {
        return new ChangeSupplierDeviceCommand(new SupplierCode(request.supplierCode().value()),
                new DeviceId(request.deviceId().value()));
    }

    public static ChangeSupplierVehicleCommand map(final ChangeSupplierVehicleApiRequest request) {
        return new ChangeSupplierVehicleCommand(new SupplierCode(request.supplierCode().value()),
                new VehicleId(request.vehicleId().value()));
    }

    public static ChangeSupplierDeliveryAreaCommand map(final ChangeSupplierDeliveryAreaApiRequest request) {
        return new ChangeSupplierDeliveryAreaCommand(new SupplierCode(request.supplierCode().value()),
                map(request.deliveryArea()));
    }

    public static SupplierBasicDataUpdateCommand map(final SupplierBasicDataUpdateApiRequest request) {
        return new SupplierBasicDataUpdateCommand(new SupplierCode(request.supplierCode().value()),
                request.firstName(), request.lastName(), request.telephoneNumber());
    }

    public static SupplierUpdateCommand map(final SupplierUpdateApiRequest request) {
        final SupplierCode supplierCode = new SupplierCode(request.supplierCode().value());
        final String firstName = request.firstName();
        final String lastName = request.lastName();
        final String telephoneNumber = request.telephoneNumber();
        final DepartmentCode departmentCode = new DepartmentCode(request.departmentCode().value());
        final VehicleId vehicleId = new VehicleId(request.vehicleId().value());
        final DeviceId deviceId = new DeviceId(request.deviceId().value());
        final DangerousGoodCertification dangerousGoodCertification = map(request.dangerousGoodCertification());
        final DriverLicense driverLicense = map(request.driverLicense());
        final DeliveryArea deliveryArea = map(request.deliveryArea());
        final Set<PackageType> supportedPackageTypes = request.supportedPackageTypes().stream()
                .map(packageTypeApi -> PackageType.valueOf(packageTypeApi.name()))
                .collect(Collectors.toSet());

        return new SupplierUpdateCommand(supplierCode, firstName, lastName, telephoneNumber, departmentCode, vehicleId,
                deviceId, dangerousGoodCertification, driverLicense, deliveryArea, supportedPackageTypes);
    }

    private static DeliveryArea map(final DeliveryAreaApi deliveryAreaApi) {
        return new DeliveryArea(deliveryAreaApi.areaName(), deliveryAreaApi.city(),
                deliveryAreaApi.district(), deliveryAreaApi.municipality(), deliveryAreaApi.region(),
                deliveryAreaApi.country(), deliveryAreaApi.postalCodes());
    }

    private static DangerousGoodCertification map(final DangerousGoodCertificationApi dangerousGoodCertificationApi) {
        return new DangerousGoodCertification(dangerousGoodCertificationApi.certificateNumber(),
                dangerousGoodCertificationApi.issueDate(), dangerousGoodCertificationApi.expiryDate(),
                dangerousGoodCertificationApi.authority(), dangerousGoodCertificationApi.valid());
    }

    public static CertificationUpdateCommand map(final CertificationUpdateApiRequest certificationUpdateRequest) {
        return new CertificationUpdateCommand(map(certificationUpdateRequest.supplierCode()),
                map(certificationUpdateRequest.dangerousGoodCertification()));
    }

    private static SupplierCode map(final SupplierCodeApi supplierCode) {
        return new SupplierCode(supplierCode.value());
    }

    public static ChangeSupplierDepartmentCodeCommand map(final ChangeSupplierDepartmentCodeApiRequest departmentCodeApiRequest) {
        return new ChangeSupplierDepartmentCodeCommand(map(departmentCodeApiRequest.supplierCode()),
                new DepartmentCode(departmentCodeApiRequest.departmentCode().value()));
    }
}
