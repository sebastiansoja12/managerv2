package com.warehouse.shipment.infrastructure.dto;

public record ShipmentPartyDto(String firstName,
                               String lastName,
                               String email,
                               String telephoneNumber,
                               String city,
                               String postalCode,
                               String street,
                               String countryCode) {
}
