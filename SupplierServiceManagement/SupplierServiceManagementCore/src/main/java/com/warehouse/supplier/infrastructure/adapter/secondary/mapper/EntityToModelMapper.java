package com.warehouse.supplier.infrastructure.adapter.secondary.mapper;

import com.warehouse.supplier.domain.model.DeliveryArea;
import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.vo.DangerousGoodCertification;
import com.warehouse.supplier.domain.vo.DriverLicense;
import com.warehouse.supplier.infrastructure.adapter.secondary.entity.SupplierEntity;

public class EntityToModelMapper {
    public static Supplier map(final SupplierEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Supplier(
                entity.getSupplierId(),
                entity.getSupplierCode(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getTelephoneNumber(),
                entity.getDepartmentCode(),
                entity.getStatus(),
                entity.getUserStatus(),
                entity.getVehicleId(),
                entity.getDeviceId(),
                map(entity.getDangerousGoodCertification()),
                map(entity.getDriverLicense()),
                map(entity.getDeliveryArea()),
                entity.getSupportedPackageTypes(),
                entity.getApiKey(),
                entity.getTermsAccepted(),
                entity.getCreatedUserId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
    
	public static DangerousGoodCertification map(
			final com.warehouse.supplier.infrastructure.adapter.secondary.entity.DangerousGoodCertification certification) {
        if (certification == null) {
            return null;
        }
		return new DangerousGoodCertification(certification.certificateNumber(), certification.issueDate(),
				certification.expiryDate(), certification.authority(), certification.valid());
    }
    
	public static DeliveryArea map(
			final com.warehouse.supplier.infrastructure.adapter.secondary.entity.DeliveryArea deliveryArea) {
		if (deliveryArea == null) {
			return null;
		}
		return new DeliveryArea(deliveryArea.areaName(), deliveryArea.city(), deliveryArea.district(),
				deliveryArea.municipality(), deliveryArea.region(), deliveryArea.country(), deliveryArea.postalCodes());
	}
    
	public static DriverLicense map(
			final com.warehouse.supplier.infrastructure.adapter.secondary.entity.DriverLicense driverLicense) {
		if (driverLicense == null) {
			return null;
		}
		return new DriverLicense(driverLicense.number(), driverLicense.acquiredDate(),
				driverLicense.drivingLicenseExpiryDate());
	}
}
