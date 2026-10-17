package com.warehouse.organisationstructure.department.infrastructure.adapter.primary.api.dto;

public record AddressApi(String city,
                         String street,
                         String postalCode,
                         String countryCode) {
}
