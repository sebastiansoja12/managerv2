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

    public Supplier() {
    }

    public Supplier(
            final SupplierId supplierId,
            final SupplierCode supplierCode,
            final String firstName,
            final String lastName,
            final String telephoneNumber,
            final DepartmentCode departmentCode,
            final SupplierStatus status,
            final UserStatus userStatus,
            final VehicleId vehicleId,
            final DeviceId deviceId,
            final DangerousGoodCertification dangerousGoodCertification,
            final DriverLicense driverLicense,
            final DeliveryArea deliveryArea,
            final Set<PackageType> supportedPackageTypes,
            final String apiKey,
            final Boolean termsAccepted,
            final Instant createdAt,
            final Instant updatedAt
    ) {
        this.supplierId = supplierId;
        this.supplierCode = supplierCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.telephoneNumber = telephoneNumber;
        this.departmentCode = departmentCode;
        this.status = status;
        this.userStatus = userStatus;
        this.vehicleId = vehicleId;
        this.deviceId = deviceId;
        this.dangerousGoodCertification = dangerousGoodCertification;
        this.driverLicense = driverLicense;
        this.deliveryArea = deliveryArea;
        this.supportedPackageTypes = supportedPackageTypes;
        this.apiKey = apiKey;
        this.termsAccepted = termsAccepted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Supplier(
            final SupplierId supplierId,
            final SupplierCode supplierCode,
            final String firstName,
            final String lastName,
            final String telephoneNumber,
            final DepartmentCode departmentCode,
            final SupplierStatus status,
            final UserStatus userStatus,
            final VehicleId vehicleId,
            final DeviceId deviceId,
            final DangerousGoodCertification dangerousGoodCertification,
            final DriverLicense driverLicense,
            final DeliveryArea deliveryArea,
            final Set<PackageType> supportedPackageTypes,
            final String apiKey,
            final Boolean termsAccepted
    ) {
        this.supplierId = supplierId;
        this.supplierCode = supplierCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.telephoneNumber = telephoneNumber;
        this.departmentCode = departmentCode;
        this.status = status;
        this.userStatus = userStatus;
        this.vehicleId = vehicleId;
        this.deviceId = deviceId;
        this.dangerousGoodCertification = dangerousGoodCertification;
        this.driverLicense = driverLicense;
        this.deliveryArea = deliveryArea;
        this.supportedPackageTypes = supportedPackageTypes;
        this.apiKey = apiKey;
        this.termsAccepted = termsAccepted;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public String getApiKey() {
        return apiKey;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public DangerousGoodCertification getDangerousGoodCertification() {
        return dangerousGoodCertification;
    }

    public DeliveryArea getDeliveryArea() {
        return deliveryArea;
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    public DeviceId getDeviceId() {
        return deviceId;
    }

    public DriverLicense getDriverLicense() {
        return driverLicense;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public SupplierStatus getStatus() {
        return status;
    }

    public SupplierCode getSupplierCode() {
        return supplierCode;
    }

    public SupplierId getSupplierId() {
        return supplierId;
    }

    public Set<PackageType> getSupportedPackageTypes() {
        return supportedPackageTypes;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public Boolean termsAccepted() {
        return termsAccepted;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public VehicleId getVehicleId() {
        return vehicleId;
    }
}
