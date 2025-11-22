package com.warehouse.supplier.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.Instant;

@Embeddable
public record DriverLicense(
        @Column(name = "driver_license_number") String number,
        @Column(name = "acquired_date") Instant acquiredDate
) {
    public DriverLicense() {
        this(null, null);
    }
}
