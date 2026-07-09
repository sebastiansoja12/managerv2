package com.warehouse.supplier.infrastructure.adapter.primary.dto;

import java.time.Instant;

public record DriverLicenseApi(String number,
                               Instant acquiredDate,
                               Instant drivingLicenseExpiryDate) {
}
