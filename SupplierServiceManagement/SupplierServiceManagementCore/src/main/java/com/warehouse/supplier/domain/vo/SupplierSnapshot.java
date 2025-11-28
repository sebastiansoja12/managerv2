package com.warehouse.supplier.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.supplier.domain.enumeration.PackageType;
import com.warehouse.supplier.domain.enumeration.SupplierStatus;
import com.warehouse.supplier.domain.enumeration.UserStatus;
import com.warehouse.supplier.domain.model.DeliveryArea;

import java.time.Instant;
import java.util.Set;

public record SupplierSnapshot(
        SupplierId supplierId,
        SupplierCode supplierCode,
        String firstName,
        String lastName,
        String telephoneNumber,
        DepartmentCode departmentCode,
        SupplierStatus status,
        UserStatus userStatus,
        VehicleId vehicleId,
        DeviceId deviceId,
        DangerousGoodCertification dangerousGoodCertification,
        DriverLicense driverLicense,
        DeliveryArea deliveryArea,
        Set<PackageType> supportedPackageTypes,
        String apiKey,
        Boolean termsAccepted,
        Instant createdAt,
        Instant updatedAt,
        UserId createdUserId
) {}

