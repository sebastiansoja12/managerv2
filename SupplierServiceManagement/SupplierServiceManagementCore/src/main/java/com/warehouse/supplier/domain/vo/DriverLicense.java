package com.warehouse.supplier.domain.vo;

import java.time.Instant;

public record DriverLicense(
        String number,
        Instant acquiredDate,
        Instant drivingLicenseExpiryDate
) {
}
