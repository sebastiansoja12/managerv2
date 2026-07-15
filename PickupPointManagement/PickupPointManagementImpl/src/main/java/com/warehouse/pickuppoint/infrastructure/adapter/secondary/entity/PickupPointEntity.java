package com.warehouse.pickuppoint.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.commonassets.model.BelongsToOperator;
import com.warehouse.pickuppoint.domain.enumeration.OpeningScheduleMode;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointCapability;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointShipmentSize;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointStatus;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "pickupPoint.PickupPointEntity")
@Table(name = "pickup_point")
public class PickupPointEntity extends BelongsToOperator {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "pickup_point_id", nullable = false))
    private PickupPointId pickupPointId;

    @Column(name = "code", nullable = false, length = 40)
    private String code;

    @Column(name = "name", nullable = false, length = 160)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 24)
    private PickupPointType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 16)
    private PickupPointStatus status;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "pickup_point_capability",
            joinColumns = @JoinColumn(name = "pickup_point_id", nullable = false))
    @Enumerated(EnumType.STRING)
    @Column(name = "capability", nullable = false, length = 24)
    private Set<PickupPointCapability> capabilities = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "country_code", length = 2)
    private CountryCode countryCode;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Column(name = "city", length = 120)
    private String city;

    @Column(name = "street", length = 160)
    private String street;

    @Column(name = "building_number", length = 30)
    private String buildingNumber;

    @Column(name = "unit_number", length = 30)
    private String unitNumber;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "department_id"))
    private DepartmentId departmentId;

    @Column(name = "telephone_number", length = 40)
    private String telephoneNumber;

    @Column(name = "email", length = 254)
    private String email;

    @Column(name = "access_instructions", length = 1000)
    private String accessInstructions;

    @Column(name = "opening_time_zone", length = 64)
    private String openingTimeZone;

    @Enumerated(EnumType.STRING)
    @Column(name = "opening_schedule_mode", length = 24)
    private OpeningScheduleMode openingScheduleMode;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "pickup_point_id", nullable = false)
    private Set<PickupPointOpeningDayEntity> openingDays = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "pickup_point_id", nullable = false)
    private Set<PickupPointScheduleExceptionEntity> scheduleExceptions = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "pickup_point_allowed_size",
            joinColumns = @JoinColumn(name = "pickup_point_id", nullable = false))
    @Enumerated(EnumType.STRING)
    @Column(name = "shipment_size", nullable = false, length = 24)
    private Set<PickupPointShipmentSize> allowedShipmentSizes = new HashSet<>();

    @Column(name = "accepts_dangerous_goods")
    private Boolean acceptsDangerousGoods;

    @Column(name = "external_network_code", length = 60)
    private String externalNetworkCode;

    @Column(name = "external_point_code", length = 120)
    private String externalPointCode;

    @Column(name = "status_reason", length = 500)
    private String statusReason;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected PickupPointEntity() {
    }

    public PickupPointEntity(
            final PickupPointId pickupPointId,
            final String code,
            final String name,
            final PickupPointType type,
            final PickupPointStatus status,
            final Set<PickupPointCapability> capabilities,
            final CountryCode countryCode,
            final String postalCode,
            final String city,
            final String street,
            final String buildingNumber,
            final String unitNumber,
            final Double latitude,
            final Double longitude,
            final DepartmentId departmentId,
            final String telephoneNumber,
            final String email,
            final String accessInstructions,
            final String openingTimeZone,
            final OpeningScheduleMode openingScheduleMode,
            final Set<PickupPointOpeningDayEntity> openingDays,
            final Set<PickupPointScheduleExceptionEntity> scheduleExceptions,
            final Set<PickupPointShipmentSize> allowedShipmentSizes,
            final Boolean acceptsDangerousGoods,
            final String externalNetworkCode,
            final String externalPointCode,
            final String statusReason,
            final Long version,
            final Instant createdAt,
            final Instant updatedAt) {
        this.pickupPointId = pickupPointId;
        this.code = code;
        this.name = name;
        this.type = type;
        this.status = status;
        this.capabilities = new HashSet<>(capabilities);
        this.countryCode = countryCode;
        this.postalCode = postalCode;
        this.city = city;
        this.street = street;
        this.buildingNumber = buildingNumber;
        this.unitNumber = unitNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.departmentId = departmentId;
        this.telephoneNumber = telephoneNumber;
        this.email = email;
        this.accessInstructions = accessInstructions;
        this.openingTimeZone = openingTimeZone;
        this.openingScheduleMode = openingScheduleMode;
        this.openingDays = new HashSet<>(openingDays);
        this.scheduleExceptions = new HashSet<>(scheduleExceptions);
        this.allowedShipmentSizes = new HashSet<>(allowedShipmentSizes);
        this.acceptsDangerousGoods = acceptsDangerousGoods;
        this.externalNetworkCode = externalNetworkCode;
        this.externalPointCode = externalPointCode;
        this.statusReason = statusReason;
        this.version = version;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public PickupPointId getPickupPointId() { return this.pickupPointId; }
    public String getCode() { return this.code; }
    public String getName() { return this.name; }
    public PickupPointType getType() { return this.type; }
    public PickupPointStatus getStatus() { return this.status; }
    public Set<PickupPointCapability> getCapabilities() { return Set.copyOf(this.capabilities); }
    public CountryCode getCountryCode() { return this.countryCode; }
    public String getPostalCode() { return this.postalCode; }
    public String getCity() { return this.city; }
    public String getStreet() { return this.street; }
    public String getBuildingNumber() { return this.buildingNumber; }
    public String getUnitNumber() { return this.unitNumber; }
    public Double getLatitude() { return this.latitude; }
    public Double getLongitude() { return this.longitude; }
    public DepartmentId getDepartmentId() { return this.departmentId; }
    public String getTelephoneNumber() { return this.telephoneNumber; }
    public String getEmail() { return this.email; }
    public String getAccessInstructions() { return this.accessInstructions; }
    public String getOpeningTimeZone() { return this.openingTimeZone; }
    public OpeningScheduleMode getOpeningScheduleMode() { return this.openingScheduleMode; }
    public Set<PickupPointOpeningDayEntity> getOpeningDays() { return Set.copyOf(this.openingDays); }
    public Set<PickupPointScheduleExceptionEntity> getScheduleExceptions() {
        return Set.copyOf(this.scheduleExceptions);
    }
    public Set<PickupPointShipmentSize> getAllowedShipmentSizes() {
        return Set.copyOf(this.allowedShipmentSizes);
    }
    public Boolean getAcceptsDangerousGoods() { return this.acceptsDangerousGoods; }
    public String getExternalNetworkCode() { return this.externalNetworkCode; }
    public String getExternalPointCode() { return this.externalPointCode; }
    public String getStatusReason() { return this.statusReason; }
    public long getVersion() { return this.version == null ? 0 : this.version; }
    public Instant getCreatedAt() { return this.createdAt; }
    public Instant getUpdatedAt() { return this.updatedAt; }
}
