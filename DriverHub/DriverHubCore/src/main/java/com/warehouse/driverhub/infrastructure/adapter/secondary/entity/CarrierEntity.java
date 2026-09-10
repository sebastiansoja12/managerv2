package com.warehouse.driverhub.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.CarrierId;
import com.warehouse.commonassets.identificator.TaxId;
import com.warehouse.commonassets.model.BelongsToOperator;
import com.warehouse.driverhub.domain.enumeration.TransportType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "driverhub.CarrierEntity")
@Table(name = "carriers")
public class CarrierEntity extends BelongsToOperator {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "carrier_id", nullable = false))
    private CarrierId carrierId;

    @Column(name = "name", nullable = false)
    private String name;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "tax_id", length = 32))
    private TaxId taxId;

    @Column(name = "phone", length = 40)
    private String phone;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "default_transport_type", nullable = false, length = 32)
    private TransportType defaultTransportType;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "carrier_id", referencedColumnName = "carrier_id", nullable = false)
    private List<CarrierDriverEntity> drivers = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "carrier_id", referencedColumnName = "carrier_id", nullable = false)
    private List<CarrierVehicleEntity> vehicles = new ArrayList<>();

    protected CarrierEntity() {
    }

    public CarrierEntity(final CarrierId carrierId,
                         final String name,
                         final TaxId taxId,
                         final String phone,
                         final String email,
                         final TransportType defaultTransportType,
                         final boolean active,
                         final Instant createdAt,
                         final List<CarrierDriverEntity> drivers,
                         final List<CarrierVehicleEntity> vehicles) {
        this.carrierId = carrierId;
        this.name = name;
        this.taxId = taxId;
        this.phone = phone;
        this.email = email;
        this.defaultTransportType = defaultTransportType;
        this.active = active;
        this.createdAt = createdAt;
        this.drivers = new ArrayList<>(drivers);
        this.vehicles = new ArrayList<>(vehicles);
    }

    public CarrierId getCarrierId() {
        return carrierId;
    }

    public String getName() {
        return name;
    }

    public TaxId getTaxId() {
        return taxId;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public TransportType getDefaultTransportType() {
        return defaultTransportType;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public List<CarrierDriverEntity> getDrivers() {
        return drivers;
    }

    public List<CarrierVehicleEntity> getVehicles() {
        return vehicles;
    }
}
