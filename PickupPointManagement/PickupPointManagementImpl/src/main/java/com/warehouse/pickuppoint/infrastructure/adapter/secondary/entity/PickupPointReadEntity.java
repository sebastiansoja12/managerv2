package com.warehouse.pickuppoint.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.commonassets.model.BelongsToOperator;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointCapability;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointShipmentSize;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointStatus;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Entity(name = "pickupPoint.PickupPointReadEntity")
@Table(name = "pickup_point_rd")
public class PickupPointReadEntity extends BelongsToOperator {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "pickup_point_id", nullable = false))
    private PickupPointId pickupPointId;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PickupPointType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PickupPointStatus status;

    @Column(name = "capabilities", nullable = false)
    private String capabilities;

    @Enumerated(EnumType.STRING)
    @Column(name = "country_code")
    private CountryCode countryCode;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String street;

    @Column(name = "building_number")
    private String buildingNumber;

    @Column(name = "unit_number")
    private String unitNumber;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "department_id"))
    private DepartmentId departmentId;

    @Column(name = "allowed_shipment_sizes", nullable = false)
    private String allowedShipmentSizes;

    @Column(name = "accepts_dangerous_goods")
    private Boolean acceptsDangerousGoods;

    @Column(name = "external_network_code")
    private String externalNetworkCode;

    @Column(name = "version", nullable = false)
    private Long version;

    protected PickupPointReadEntity() {
    }

    public PickupPointReadEntity(
            final PickupPointId pickupPointId,
            final String code,
            final String name,
            final PickupPointType type,
            final PickupPointStatus status,
            final String capabilities,
            final CountryCode countryCode,
            final String postalCode,
            final String city,
            final String street,
            final String buildingNumber,
            final String unitNumber,
            final Double latitude,
            final Double longitude,
            final DepartmentId departmentId,
            final String allowedShipmentSizes,
            final Boolean acceptsDangerousGoods,
            final String externalNetworkCode,
            final Long version) {
        this.pickupPointId = pickupPointId;
        this.code = code;
        this.name = name;
        this.type = type;
        this.status = status;
        this.capabilities = capabilities;
        this.countryCode = countryCode;
        this.postalCode = postalCode;
        this.city = city;
        this.street = street;
        this.buildingNumber = buildingNumber;
        this.unitNumber = unitNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.departmentId = departmentId;
        this.allowedShipmentSizes = allowedShipmentSizes;
        this.acceptsDangerousGoods = acceptsDangerousGoods;
        this.externalNetworkCode = externalNetworkCode;
        this.version = version;
    }

    public PickupPointId getPickupPointId() {
        return this.pickupPointId;
    }

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public PickupPointType getType() {
        return this.type;
    }

    public PickupPointStatus getStatus() {
        return this.status;
    }

    public Set<PickupPointCapability> getCapabilities() {
        return enumValues(this.capabilities, PickupPointCapability::valueOf);
    }

    public CountryCode getCountryCode() {
        return this.countryCode;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public String getCity() {
        return this.city;
    }

    public String getStreet() {
        return this.street;
    }

    public String getBuildingNumber() {
        return this.buildingNumber;
    }

    public String getUnitNumber() {
        return this.unitNumber;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public DepartmentId getDepartmentId() {
        return this.departmentId;
    }

    public Set<PickupPointShipmentSize> getAllowedShipmentSizes() {
        return enumValues(this.allowedShipmentSizes, PickupPointShipmentSize::valueOf);
    }

    public boolean acceptsDangerousGoods() {
        return Boolean.TRUE.equals(this.acceptsDangerousGoods);
    }

    public String getExternalNetworkCode() {
        return this.externalNetworkCode;
    }

    public long getVersion() {
        return this.version == null ? 0 : this.version;
    }

    private <T> Set<T> enumValues(final String values, final Function<String, T> mapper) {
        if (values.isBlank()) {
            return Set.of();
        }
        return Arrays.stream(values.split(","))
                .map(mapper)
                .collect(Collectors.toUnmodifiableSet());
    }
}
