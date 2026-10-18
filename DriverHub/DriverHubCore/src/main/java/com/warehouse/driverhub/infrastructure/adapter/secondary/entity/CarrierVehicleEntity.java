package com.warehouse.driverhub.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.CarrierId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.driverhub.domain.enumeration.VehicleType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity(name = "driverhub.CarrierVehicleEntity")
@Table(name = "carrier_vehicles")
public class CarrierVehicleEntity {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "vehicle_id", nullable = false))
    private VehicleId vehicleId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "carrier_id", nullable = false,
            insertable = false, updatable = false))
    private CarrierId carrierId;

    @Column(name = "registration_number", nullable = false, length = 32)
    private String registrationNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false, length = 32)
    private VehicleType vehicleType;

    @Column(name = "trailer_registration_number", length = 32)
    private String trailerRegistrationNumber;

    @Column(name = "max_load_kg", precision = 12, scale = 3)
    private BigDecimal maxLoadKg;

    @Column(name = "active", nullable = false)
    private boolean active;

    protected CarrierVehicleEntity() {
    }

    public CarrierVehicleEntity(final VehicleId vehicleId,
                                final CarrierId carrierId,
                                final String registrationNumber,
                                final VehicleType vehicleType,
                                final String trailerRegistrationNumber,
                                final BigDecimal maxLoadKg,
                                final boolean active) {
        this.vehicleId = vehicleId;
        this.carrierId = carrierId;
        this.registrationNumber = registrationNumber;
        this.vehicleType = vehicleType;
        this.trailerRegistrationNumber = trailerRegistrationNumber;
        this.maxLoadKg = maxLoadKg;
        this.active = active;
    }

    public VehicleId getVehicleId() {
        return vehicleId;
    }

    public CarrierId getCarrierId() {
        return carrierId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public String getTrailerRegistrationNumber() {
        return trailerRegistrationNumber;
    }

    public BigDecimal getMaxLoadKg() {
        return maxLoadKg;
    }

    public boolean isActive() {
        return active;
    }
}
