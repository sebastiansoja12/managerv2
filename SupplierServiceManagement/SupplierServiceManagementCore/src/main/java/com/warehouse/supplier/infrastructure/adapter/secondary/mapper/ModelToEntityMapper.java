package com.warehouse.supplier.infrastructure.adapter.secondary.mapper;

import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.infrastructure.adapter.secondary.entity.DangerousGoodCertification;
import com.warehouse.supplier.infrastructure.adapter.secondary.entity.DeliveryArea;
import com.warehouse.supplier.infrastructure.adapter.secondary.entity.DriverLicense;
import com.warehouse.supplier.infrastructure.adapter.secondary.entity.SupplierEntity;

public class ModelToEntityMapper {

    public static SupplierEntity map(final Supplier supplier) {
        if (supplier == null) {
            return null;
        }

        return new SupplierEntity(
                supplier.getSupplierId(),
                supplier.supplierCode(),
                supplier.getDepartmentCode(),
                supplier.getFirstName(),
                supplier.getLastName(),
                supplier.getTelephoneNumber(),
                supplier.getStatus(),
                supplier.getUserStatus(),
                supplier.getVehicleId(),
                supplier.getDeviceId(),
                map(supplier.getDangerousGoodCertification()),
                map(supplier.getDriverLicense()),
                map(supplier.getDeliveryArea()),
                supplier.getSupportedPackageTypes(),
                supplier.getApiKey(),
                supplier.termsAccepted(),
                supplier.createdUserId(),
                supplier.getCreatedAt(),
                supplier.getUpdatedAt()
        );
    }

    public static DangerousGoodCertification map(
            final com.warehouse.supplier.domain.vo.DangerousGoodCertification certification) {
        if (certification == null) {
            return null;
        }
        return new DangerousGoodCertification(certification.certificateNumber(), certification.issueDate(),
                certification.expiryDate(), certification.authority(), certification.valid());
    }

    public static DeliveryArea map(final com.warehouse.supplier.domain.model.DeliveryArea deliveryArea) {
        if (deliveryArea == null) {
            return null;
        }
        return new DeliveryArea(deliveryArea.getAreaName(), deliveryArea.getCity(), deliveryArea.getDistrict(),
                deliveryArea.getMunicipality(), deliveryArea.getRegion(), deliveryArea.getCountry(), deliveryArea.getPostalCodes());
    }

    public static DriverLicense map(final com.warehouse.supplier.domain.vo.DriverLicense driverLicense) {
        if (driverLicense == null) {
            return null;
        }
        return new DriverLicense(driverLicense.number(), driverLicense.acquiredDate(),
                driverLicense.drivingLicenseExpiryDate());
    }
}


