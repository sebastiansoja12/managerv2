package com.warehouse.supplier.infrastructure.adapter.secondary.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record DriverLicense(
        @Column(name = "driver_license_number") String number,
        @Column(name = "acquired_date") Instant acquiredDate,
        @Column(name = "driving_license_expiry_date") Instant drivingLicenseExpiryDate
) {
    public DriverLicense() {
        this(null, null, null);
    }
}
