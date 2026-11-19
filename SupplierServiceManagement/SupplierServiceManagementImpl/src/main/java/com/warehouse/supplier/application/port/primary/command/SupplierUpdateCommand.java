package com.warehouse.supplier.application.port.primary.command;

import com.warehouse.commonassets.enumeration.PackageType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.supplier.domain.vo.DangerousGoodCertification;
import com.warehouse.supplier.domain.vo.DeliveryArea;
import com.warehouse.supplier.domain.vo.DriverLicense;

import java.util.Set;

public record SupplierUpdateCommand(SupplierCode supplierCode, String firstName, String lastName,
                                    String telephoneNumber, DepartmentCode departmentCode, VehicleId vehicleId, DeviceId deviceId,
                                    DangerousGoodCertification dangerousGoodCertification, DriverLicense driverLicense,
                                    DeliveryArea deliveryArea, Set<PackageType> supportedPackageTypes) {
}
