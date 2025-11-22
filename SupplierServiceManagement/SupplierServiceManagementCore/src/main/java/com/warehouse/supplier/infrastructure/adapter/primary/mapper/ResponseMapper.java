package com.warehouse.supplier.infrastructure.adapter.primary.mapper;

import com.warehouse.supplier.domain.model.DeliveryArea;
import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.vo.DangerousGoodCertification;
import com.warehouse.supplier.domain.vo.DriverLicense;
import com.warehouse.supplier.domain.vo.SupplierCreateResponse;
import com.warehouse.supplier.infrastructure.adapter.primary.dto.*;

public abstract class ResponseMapper {

    public static SupplierCreateApiResponse map(final SupplierCreateResponse response) {
        return new SupplierCreateApiResponse(response.supplierCode().value());
    }

    public static SupplierApi map(final Supplier supplier) {
        if (supplier == null) {
            return null;
        }

        return new SupplierApi(
                supplier.supplierCode() != null ? new SupplierCodeApi(supplier.supplierCode().value()) : null,
                supplier.getFirstName(),
                supplier.getLastName(),
                supplier.getTelephoneNumber(),
                supplier.getDepartmentCode() != null ? new DepartmentCodeApi(supplier.getDepartmentCode().getValue()) : null,
                supplier.getStatus() != null ? supplier.getStatus().name() : null,
                supplier.getUserStatus() != null ? supplier.getUserStatus().name() : null,
                supplier.getVehicleId() != null ? new VehicleIdApi(supplier.getVehicleId().value()) : null,
                supplier.getDeviceId() != null ? new DeviceIdApi(supplier.getDeviceId().getValue()) : null,
                supplier.getDangerousGoodCertification() != null ? map(supplier.getDangerousGoodCertification()) : null,
                supplier.getDriverLicense() != null ? map(supplier.getDriverLicense()) : null,
                supplier.getDeliveryArea() != null ? map(supplier.getDeliveryArea()) : null,
				supplier.getSupportedPackageTypes() != null ? supplier.getSupportedPackageTypes().stream()
						.map(pt -> PackageTypeApi.valueOf(pt.name()))
                        .collect(java.util.stream.Collectors.toSet()) : null,
                supplier.termsAccepted(),
                supplier.getCreatedAt() != null ? supplier.getCreatedAt().toString() : null,
                supplier.getUpdatedAt() != null ? supplier.getUpdatedAt().toString() : null,
                supplier.createdUserId() != null ? new UserIdApi(supplier.createdUserId().getValue()) : null
        );
    }
    
	public static DeliveryAreaApi map(final DeliveryArea deliveryArea) {
		return new DeliveryAreaApi(deliveryArea.getAreaName(), deliveryArea.getCity(), deliveryArea.getDistrict(),
				deliveryArea.getMunicipality(), deliveryArea.getRegion(), deliveryArea.getCountry(),
				deliveryArea.getPostalCodes());
	}
    
	public static DangerousGoodCertificationApi map(final DangerousGoodCertification dangerousGoodCertification) {
		return new DangerousGoodCertificationApi(dangerousGoodCertification.certificateNumber(),
				dangerousGoodCertification.issueDate(), dangerousGoodCertification.expiryDate(),
				dangerousGoodCertification.authority(), dangerousGoodCertification.valid());
	}

	public static DriverLicenseApi map(final DriverLicense driverLicense) {
		return new DriverLicenseApi(driverLicense.number(), driverLicense.acquiredDate());
	}
}
