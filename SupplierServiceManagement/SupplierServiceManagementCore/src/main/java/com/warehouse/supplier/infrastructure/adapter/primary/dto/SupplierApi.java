package com.warehouse.supplier.infrastructure.adapter.primary.dto;

import java.util.Set;

public record SupplierApi(SupplierCodeApi supplierCode,
                          String firstName,
                          String lastName,
                          String telephoneNumber,
                          DepartmentCodeApi departmentCode,
                          String status,
                          String userStatus,
                          VehicleIdApi vehicleId,
                          DeviceIdApi deviceId,
                          DangerousGoodCertificationApi dangerousGoodCertification,
                          DriverLicenseApi driverLicense,
                          DeliveryAreaApi deliveryArea,
                          Set<PackageTypeApi> supportedPackageTypes,
                          Boolean termsAccepted,
                          String createdAt,
                          String updatedAt,
                          UserIdApi createdUserId) {
}
