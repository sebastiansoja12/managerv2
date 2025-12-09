package com.warehouse.supplier.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.*;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.repository.baseentity.BaseEntity;
import com.warehouse.commonassets.enumeration.PackageType;
import com.warehouse.supplier.domain.enumeration.SupplierStatus;
import com.warehouse.supplier.domain.enumeration.UserStatus;
import jakarta.persistence.*;
import org.hibernate.envers.Audited;

import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "suppliers")
@Audited
public class SupplierEntity extends BaseEntity<SupplierId> {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "supplier_id"))
    private SupplierId supplierId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "supplier_code"))
    private SupplierCode supplierCode;

    @Embedded
    private DepartmentCode departmentCode;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "telephone_number")
    private String telephoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SupplierStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status")
    private UserStatus userStatus;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "vehicle_id"))
    private VehicleId vehicleId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "device_id"))
    private DeviceId deviceId;

    @Embedded
    private DangerousGoodCertification dangerousGoodCertification;

    @Embedded
    private DriverLicense driverLicense;

    @Embedded
    private DeliveryArea deliveryArea;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "suppliers_supported_package_types", joinColumns = @JoinColumn(name = "supplier_id"))
    @Column(name = "package_type")
    private Set<PackageType> supportedPackageTypes;

    @Column(name = "api_key")
    private String apiKey;

    @Column(name = "terms_accepted")
    private Boolean termsAccepted;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "created_user_id"))
    private UserId createdUserId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    public SupplierEntity() {
    }

    public SupplierEntity(
            final SupplierId supplierId,
            final SupplierCode supplierCode,
            final DepartmentCode departmentCode,
            final String firstName,
            final String lastName,
            final String telephoneNumber,
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
        this.departmentCode = departmentCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.telephoneNumber = telephoneNumber;
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


    public String getApiKey() {
        return apiKey;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public UserId getCreatedUserId() {
        return createdUserId;
    }

    public DangerousGoodCertification getDangerousGoodCertification() {
        return dangerousGoodCertification;
    }

    public DeliveryArea getDeliveryArea() {
        return deliveryArea;
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

    public Boolean getTermsAccepted() {
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

    @Override
    public SupplierId getId() {
        return supplierId;
    }

    @Override
    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }
}

