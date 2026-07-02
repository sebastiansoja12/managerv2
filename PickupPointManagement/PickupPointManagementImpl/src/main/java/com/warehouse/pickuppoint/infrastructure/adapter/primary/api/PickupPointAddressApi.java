package com.warehouse.pickuppoint.infrastructure.adapter.primary.api;

import jakarta.validation.constraints.NotBlank;

public record PickupPointAddressApi(
        @NotBlank String countryCode,
        @NotBlank String postalCode,
        @NotBlank String city,
        @NotBlank String street,
        @NotBlank String buildingNumber,
        String unitNumber) {
}
