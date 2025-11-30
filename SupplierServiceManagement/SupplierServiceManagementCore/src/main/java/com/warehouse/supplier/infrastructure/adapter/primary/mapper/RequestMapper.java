package com.warehouse.supplier.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.enumeration.PackageType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.supplier.domain.vo.*;
import com.warehouse.supplier.infrastructure.adapter.primary.dto.*;

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
}
