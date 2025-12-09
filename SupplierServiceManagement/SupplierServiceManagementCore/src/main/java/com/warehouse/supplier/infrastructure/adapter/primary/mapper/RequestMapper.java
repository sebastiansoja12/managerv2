package com.warehouse.supplier.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.enumeration.PackageType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.supplier.domain.model.DeliveryArea;
import com.warehouse.supplier.domain.vo.*;
import com.warehouse.supplier.infrastructure.adapter.primary.dto.*;

import java.util.Set;
import java.util.stream.Collectors;

public abstract class RequestMapper {

    public static SupplierCreateRequest map(final SupplierCreateApiRequest request) {
        return new SupplierCreateRequest(request.supplierCode(), request.firstName(),
                request.lastName(), request.telephoneNumber());
    }

    public static DriverLicenseRequest map(final DriverLicenseApiRequest request) {
        return new DriverLicenseRequest(request.supplierCode(), map(request.driverLicense()));
    }

    public static DriverLicense map(final DriverLicenseApi driverLicense) {
        return new DriverLicense(driverLicense.number(), driverLicense.acquiredDate(), driverLicense.drivingLicenseExpiryDate());
    }

    public static ChangeSupportedPackageTypeRequest map(final ChangeSupportedPackageTypesApiRequest request) {
        return new ChangeSupportedPackageTypeRequest(new SupplierCode(request.supplierCode().value()),
                PackageType.valueOf(request.packageType()));
    }

    public static ChangeSupplierDeviceRequest map(final ChangeSupplierDeviceApiRequest request) {
        return new ChangeSupplierDeviceRequest(new SupplierCode(request.supplierCode().value()),
                new DeviceId(request.deviceId().value()));
    }

    public static SupplierUpdateRequest map(final SupplierUpdateApiRequest request) {
        final SupplierCode supplierCode = new SupplierCode(request.supplierCode().value());
        final String firstName = request.firstName();
        final String lastName = request.lastName();
        final String telephoneNumber = request.telephoneNumber();
        final VehicleId vehicleId = new VehicleId(request.vehicleId().value());
        final DeviceId deviceId = new DeviceId(request.deviceId().value());
        final DangerousGoodCertification dangerousGoodCertification = map(request.dangerousGoodCertification());
        final DriverLicense driverLicense = map(request.driverLicense());
        final DeliveryArea deliveryArea = map(request.deliveryArea());
        final Set<PackageType> supportedPackageTypes = request.supportedPackageTypes().stream()
                .map(packageTypeApi -> PackageType.valueOf(packageTypeApi.name()))
                .collect(Collectors.toSet());

        return new SupplierUpdateRequest(supplierCode, firstName, lastName, telephoneNumber, vehicleId,
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

    public static CertificationUpdateRequest map(final CertificationUpdateApiRequest certificationUpdateRequest) {
        return new CertificationUpdateRequest(map(certificationUpdateRequest.supplierCode()),
                map(certificationUpdateRequest.dangerousGoodCertification()));
    }

    private static SupplierCode map(final SupplierCodeApi supplierCode) {
        return new SupplierCode(supplierCode.value());
    }
}
