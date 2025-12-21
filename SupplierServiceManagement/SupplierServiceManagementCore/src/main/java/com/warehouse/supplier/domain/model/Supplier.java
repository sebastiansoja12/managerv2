package com.warehouse.supplier.domain.model;

import java.time.Instant;
import java.util.Set;

import org.apache.commons.lang3.Validate;

import com.google.common.collect.Sets;
import com.warehouse.commonassets.enumeration.PackageType;
import com.warehouse.commonassets.identificator.*;
import com.warehouse.supplier.domain.enumeration.SupplierStatus;
import com.warehouse.supplier.domain.enumeration.UserStatus;
import com.warehouse.supplier.domain.vo.DangerousGoodCertification;
import com.warehouse.supplier.domain.vo.DriverLicense;
import com.warehouse.supplier.domain.vo.SupplierDto;
import com.warehouse.supplier.domain.vo.SupplierSnapshot;


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

    private UserId createdUserId;

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
            final UserId createdUserId,
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
        this.createdUserId = createdUserId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Supplier(
            final SupplierId supplierId,
            final SupplierCode supplierCode,
            final String firstName,
            final String lastName,
            final String telephoneNumber
    ) {
        Validate.notNull(supplierId, "SupplierId cannot be null");
        Validate.notNull(supplierCode, "SupplierCode cannot be null");
        Validate.notNull(firstName, "First name cannot be null");
        Validate.notNull(lastName, "Last name cannot be null");
        Validate.notNull(telephoneNumber, "Telephone number cannot be null");

        this.supplierId = supplierId;
        this.supplierCode = supplierCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.telephoneNumber = telephoneNumber;
        this.departmentCode = null;
        this.status = SupplierStatus.INACTIVE;
        this.userStatus = UserStatus.USER_NOT_CREATED;
        this.vehicleId = null;
        this.deviceId = null;
        this.dangerousGoodCertification = null;
        this.driverLicense = null;
        this.deliveryArea = null;
        this.supportedPackageTypes = Sets.newHashSet();
        this.apiKey = null;
        this.termsAccepted = false;
        this.createdUserId = null;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }
    
	public SupplierSnapshot snapshot() {
		return new SupplierSnapshot(supplierId, supplierCode, firstName, lastName, telephoneNumber, departmentCode,
				status, userStatus, vehicleId, deviceId, dangerousGoodCertification, driverLicense, deliveryArea,
				supportedPackageTypes, apiKey, termsAccepted, createdAt, updatedAt, createdUserId);
	}

    public void markAsActive() {
        if (this.status == SupplierStatus.DELETED) {
            throw new IllegalArgumentException("Cannot activate deleted supplier");
        }
        if (driverLicense == null) {
            throw new IllegalArgumentException("Cannot activate supplier when driver license is null");
        }
        this.status = SupplierStatus.ACTIVE;
    }

    public void markAsInactive() {
        if (this.status == SupplierStatus.DELETED) {
            throw new IllegalArgumentException("Cannot deactivate deleted supplier");
        }
        this.status = SupplierStatus.INACTIVE;
    }

    public void markAsDeleted() {
        this.status = SupplierStatus.DELETED;
    }

    public void markAsSuspended() {
        this.status = SupplierStatus.SUSPENDED;
    }

    public void changeDriverLicense(final DriverLicense driverLicense) {
        this.driverLicense = driverLicense;
        markAsModified();
    }

    public void markUserCreated(final UserId createdUserId) {
        this.userStatus = UserStatus.USER_CREATED;
        this.createdUserId = createdUserId;
        markAsModified();
    }

    public void markUserDeleted() {
        this.userStatus = UserStatus.USER_NOT_CREATED;
        this.createdUserId = null;
        markAsModified();
    }

    public void changeTermsAccepted(final Boolean termsAccepted) {
        this.termsAccepted = termsAccepted;
        markAsModified();
    }

    public void changeApiKey(final String apiKey) {
        this.apiKey = apiKey;
        markAsModified();
    }

    public void changeDeliveryArea(final DeliveryArea deliveryArea) {
        this.deliveryArea = deliveryArea;
        markAsModified();
    }

    public void changeSupportedPackageTypes(final Set<PackageType> supportedPackageTypes) {
        this.supportedPackageTypes = supportedPackageTypes;
        markAsModified();
    }

    public void addSupportedPackageType(final PackageType packageType) {
        getSupportedPackageTypes().add(packageType);
        markAsModified();
    }

    public void removeSupportedPackageType(final PackageType packageType) {
        getSupportedPackageTypes().remove(packageType);
        markAsModified();
    }

    public void changeDangerousGoodCertification(final DangerousGoodCertification dangerousGoodCertification) {
        this.dangerousGoodCertification = dangerousGoodCertification;
        markAsModified();
    }

    public void assignVehicle(final VehicleId vehicleId) {
        this.vehicleId = vehicleId;
        markAsModified();
    }

    public void changeDeviceId(final DeviceId deviceId) {
        this.deviceId = deviceId;
        markAsModified();
    }

    public void updateData(final SupplierDto supp) {
        if (supp.deviceId() != null) {
            changeDeviceId(supp.deviceId());
        }

        if (supp.driverLicense() != null) {
            changeDriverLicense(supp.driverLicense());
        }

        if (supp.deliveryArea() != null) {
            changeDeliveryArea(supp.deliveryArea());
        }

        if (supp.supportedPackageTypes() != null) {
            changeSupportedPackageTypes(supp.supportedPackageTypes());
        }

        if (supp.dangerousGoodCertification() != null) {
            changeDangerousGoodCertification(supp.dangerousGoodCertification());
        }

        if (supp.vehicleId() != null) {
            assignVehicle(supp.vehicleId());
        }

        acceptTerms();

        validateStateRequiredForActivation();
        changeStatus(SupplierStatus.ACTIVE);
        markAsModified();
    }

    private void acceptTerms() {
        this.termsAccepted = true;
        markAsModified();
    }

    private void validateStateRequiredForActivation() {
        Validate.notNull(supplierId, "SupplierId cannot be null");
        Validate.notNull(supplierCode, "SupplierCode cannot be null");

        Validate.notNull(firstName, "First name cannot be null");
        Validate.notNull(lastName, "Last name cannot be null");
        Validate.notNull(telephoneNumber, "Telephone number cannot be null");

        Validate.notNull(departmentCode, "DepartmentCode must be specified for activation");
        Validate.notNull(vehicleId, "VehicleId must be specified for activation");
        Validate.notNull(deviceId, "DeviceId must be specified for activation");
        Validate.notNull(dangerousGoodCertification, "Dangerous good certification must be provided");
        Validate.notNull(driverLicense, "Driver license must be provided");
        Validate.notNull(deliveryArea, "Delivery area must be provided");

        Validate.isTrue(!supportedPackageTypes.isEmpty(),
                "Supported package types must contain at least one element");

        Validate.isTrue(termsAccepted, "Terms must be accepted before activation");

        Validate.notNull(createdAt, "CreatedAt cannot be null");
        Validate.notNull(updatedAt, "UpdatedAt cannot be null");
    }

    private void changeStatus(final SupplierStatus supplierStatus) {
        if (this.status == SupplierStatus.DELETED) {
            throw new IllegalArgumentException("Cannot change status of deleted supplier");
        }
        this.status = supplierStatus;
        markAsModified();
    }

    public void markDriverLicenseAsInvalid() {
        if (this.driverLicense != null) {
            changeStatus(SupplierStatus.SUSPENDED);
            this.driverLicense = null;
        }
    }

    public void markCertificationAsInvalid() {
        if (this.dangerousGoodCertification != null) {
            this.dangerousGoodCertification = null;
        }
    }

    private void markAsModified() {
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

    public SupplierCode supplierCode() {
        return supplierCode;
    }

    public SupplierId getSupplierId() {
        return supplierId;
    }

    public Set<PackageType> getSupportedPackageTypes() {
        if (supportedPackageTypes == null) {
            supportedPackageTypes = Sets.newHashSet();
        }
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

    public UserId createdUserId() {
        return createdUserId;
    }
}
