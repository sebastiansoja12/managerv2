package com.warehouse.supplier.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.enumeration.PackageType;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.supplier.domain.vo.*;
import com.warehouse.supplier.infrastructure.adapter.primary.dto.ChangeSupportedPackageTypesApiRequest;
import com.warehouse.supplier.infrastructure.adapter.primary.dto.DriverLicenseApi;
import com.warehouse.supplier.infrastructure.adapter.primary.dto.DriverLicenseApiRequest;
import com.warehouse.supplier.infrastructure.adapter.primary.dto.SupplierCreateApiRequest;

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
}
