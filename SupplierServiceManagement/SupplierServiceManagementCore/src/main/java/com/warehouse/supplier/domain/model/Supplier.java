package com.warehouse.supplier.domain.model;

import com.warehouse.supplier.domain.enumeration.PackageType;
import com.warehouse.supplier.domain.enumeration.SupplierStatus;
import com.warehouse.supplier.domain.enumeration.UserStatus;
import com.warehouse.supplier.domain.vo.*;

import java.time.Instant;
import java.util.Set;


public class Supplier {

    private SupplierId supplierId;

    private SupplierCode supplierCode;

    private String firstName;

    private String lastName;

    private String telephoneNumber;

    private DepartmentCode departmentCode;

    private SupplierStatus status;

    private UserStatus userStatus;

    private VehicleId vehicleId;

    private DeviceId deviceId;

    private DangerousGoodCertification dangerousGoodCertification;

    private DriverLicense driverLicense;

    private DeliveryArea deliveryArea;

    private Set<PackageType> supportedPackageTypes;

    private String apiKey;

    private Boolean termsAccepted;

    private Instant createdAt;

    private Instant updatedAt;


}
