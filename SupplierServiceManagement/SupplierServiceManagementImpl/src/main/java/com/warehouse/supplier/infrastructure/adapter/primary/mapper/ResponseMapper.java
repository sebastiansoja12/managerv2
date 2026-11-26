package com.warehouse.supplier.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.supplier.application.port.primary.result.CertificationUpdateResponse;
import com.warehouse.supplier.application.port.primary.result.DriverLicenseResponse;
import com.warehouse.supplier.application.port.primary.result.SupplierCreateResponse;
import com.warehouse.supplier.application.port.primary.result.SupplierResult;
import com.warehouse.supplier.application.port.primary.result.SupplierUpdateResponse;
import com.warehouse.supplier.domain.vo.DangerousGoodCertification;
import com.warehouse.supplier.domain.vo.DeliveryArea;
import com.warehouse.supplier.domain.vo.DriverLicense;
import com.warehouse.supplier.domain.vo.SupplierSnapshot;
import com.warehouse.supplier.infrastructure.adapter.primary.dto.*;

import java.util.stream.Collectors;

public abstract class ResponseMapper {

    public static SupplierCreateApiResponse map(final SupplierCreateResponse response) {
        return new SupplierCreateApiResponse(response.supplierCode().value());
    }

    public static SupplierApi map(final SupplierResult result) {
        final SupplierSnapshot supplier = result.supplier();
        final DepartmentCode departmentCode = result.departmentCode();
        return new SupplierApi(
                supplier.supplierId() != null ? supplier.supplierId().value() : null,
                supplier.supplierCode() != null ? new SupplierCodeApi(supplier.supplierCode().value()) : null,
                supplier.firstName(),
                supplier.lastName(),
                supplier.telephoneNumber(),
                supplier.email(),
                departmentCode != null ? new DepartmentCodeApi(departmentCode.getValue()) : null,
                supplier.status() != null ? supplier.status().name() : null,
                supplier.userStatus() != null ? supplier.userStatus().name() : null,
                supplier.vehicleId() != null ? new VehicleIdApi(supplier.vehicleId().value()) : null,
                supplier.deviceId() != null ? new DeviceIdApi(supplier.deviceId().getValue()) : null,
                supplier.dangerousGoodCertification() != null ? map(supplier.dangerousGoodCertification()) : null,
                supplier.driverLicense() != null ? map(supplier.driverLicense()) : null,
                supplier.deliveryArea() != null ? map(supplier.deliveryArea()) : null,
                supplier.supportedPackageTypes() != null ? supplier.supportedPackageTypes().stream()
                        .map(packageType -> PackageTypeApi.valueOf(packageType.name()))
                        .collect(Collectors.toSet()) : null,
                supplier.termsAccepted(),
                supplier.createdAt() != null ? supplier.createdAt().toString() : null,
                supplier.updatedAt() != null ? supplier.updatedAt().toString() : null,
                supplier.createdUserId() != null ? new UserIdApi(supplier.createdUserId().getValue()) : null
        );
    }

    public static DeliveryAreaApi map(final DeliveryArea deliveryArea) {
        return new DeliveryAreaApi(deliveryArea.areaName(), deliveryArea.city(), deliveryArea.district(),
                deliveryArea.municipality(), deliveryArea.region(), deliveryArea.country(),
                deliveryArea.postalCodes());
    }

	public static DangerousGoodCertificationApi map(final DangerousGoodCertification dangerousGoodCertification) {
		return new DangerousGoodCertificationApi(dangerousGoodCertification.certificateNumber(),
				dangerousGoodCertification.issueDate(), dangerousGoodCertification.expiryDate(),
				dangerousGoodCertification.authority(), dangerousGoodCertification.valid());
	}

	public static DriverLicenseApi map(final DriverLicense driverLicense) {
		return new DriverLicenseApi(driverLicense.number(), driverLicense.acquiredDate(), driverLicense.drivingLicenseExpiryDate());
	}

    public static DriverLicenseApiResponse map(final DriverLicenseResponse response) {
        return new DriverLicenseApiResponse(response.status().name(), response.message());
    }

    public static SupplierUpdateApiResponse map(final SupplierUpdateResponse response) {
        return new SupplierUpdateApiResponse(new SupplierCodeApi(response.supplierCode().value()),
                String.valueOf(response.status()), response.message());
    }

    public static CertificationUpdateApiResponse map(final CertificationUpdateResponse response) {
        return new CertificationUpdateApiResponse(response.status().name(), response.message());
    }
}
