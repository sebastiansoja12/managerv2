package com.warehouse.supplier.infrastructure.adapter.primary.dto;

import java.util.Set;

public record SupplierUpdateApiRequest(
        SupplierCodeApi supplierCode, String firstName, String lastName,
        String telephoneNumber, VehicleIdApi vehicleId, DeviceIdApi deviceId,
        DangerousGoodCertificationApi dangerousGoodCertification, DriverLicenseApi driverLicense,
        DeliveryAreaApi deliveryArea, Set<PackageTypeApi> supportedPackageTypes
) {
}
