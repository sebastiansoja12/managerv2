package com.warehouse.supplier.domain.vo;

import com.warehouse.commonassets.enumeration.PackageType;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.VehicleId;

import java.util.Set;

public record SupplierActivationData(DepartmentId departmentId,
                                     VehicleId vehicleId,
                                     DeviceId deviceId,
                                     DangerousGoodCertification dangerousGoodCertification,
                                     DriverLicense driverLicense,
                                     DeliveryArea deliveryArea,
                                     Set<PackageType> supportedPackageTypes) {
}
