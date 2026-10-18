package com.warehouse.driverhub.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.CarrierId;
import com.warehouse.commonassets.identificator.DriverId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity(name = "driverhub.CarrierDriverEntity")
@Table(name = "carrier_drivers")
public class CarrierDriverEntity {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "driver_id", nullable = false))
    private DriverId driverId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "carrier_id", nullable = false,
            insertable = false, updatable = false))
    private CarrierId carrierId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "phone", length = 40)
    private String phone;

    @Column(name = "license_number", length = 64)
    private String licenseNumber;

    @Column(name = "active", nullable = false)
    private boolean active;

    protected CarrierDriverEntity() {
    }

    public CarrierDriverEntity(final DriverId driverId,
                               final CarrierId carrierId,
                               final String firstName,
                               final String lastName,
                               final String phone,
                               final String licenseNumber,
                               final boolean active) {
        this.driverId = driverId;
        this.carrierId = carrierId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.licenseNumber = licenseNumber;
        this.active = active;
    }

    public DriverId getDriverId() {
        return driverId;
    }

    public CarrierId getCarrierId() {
        return carrierId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public boolean isActive() {
        return active;
    }
}
