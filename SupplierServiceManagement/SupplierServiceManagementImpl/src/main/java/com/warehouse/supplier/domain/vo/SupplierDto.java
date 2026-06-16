package com.warehouse.supplier.domain.vo;

import com.warehouse.commonassets.enumeration.PackageType;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.supplier.domain.model.DeliveryArea;

import java.util.Set;

public record SupplierDto(String firstName, String lastName,
                          String telephoneNumber, DepartmentId departmentId,
                          VehicleId vehicleId, DeviceId deviceId,
                          DangerousGoodCertification dangerousGoodCertification, DriverLicense driverLicense,
                          DeliveryArea deliveryArea, Set<PackageType> supportedPackageTypes) {

    public static SupplierDto from(final SupplierUpdateCommand request, final DepartmentId departmentId) {
        return new SupplierDto(request.firstName(), request.lastName(), request.telephoneNumber(),
                departmentId, request.vehicleId(), request.deviceId(),
                request.dangerousGoodCertification(), request.driverLicense(),
                request.deliveryArea(), request.supportedPackageTypes());
    }
}
