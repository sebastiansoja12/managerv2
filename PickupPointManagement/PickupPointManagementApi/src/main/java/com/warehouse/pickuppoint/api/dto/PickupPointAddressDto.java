package com.warehouse.pickuppoint.api.dto;

public record PickupPointAddressDto(
        String countryCode,
        String postalCode,
        String city,
        String street,
        String buildingNumber,
        String unitNumber) {
}
