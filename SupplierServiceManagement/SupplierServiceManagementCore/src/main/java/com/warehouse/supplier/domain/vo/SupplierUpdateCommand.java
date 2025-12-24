package com.warehouse.supplier.domain.vo;

import com.warehouse.commonassets.enumeration.PackageType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.supplier.domain.model.DeliveryArea;

import java.util.Set;

public record SupplierUpdateCommand(SupplierCode supplierCode, String firstName, String lastName,
                                    String telephoneNumber, VehicleId vehicleId, DeviceId deviceId,
                                    DangerousGoodCertification dangerousGoodCertification, DriverLicense driverLicense,
                                    DeliveryArea deliveryArea, Set<PackageType> supportedPackageTypes) {
}
