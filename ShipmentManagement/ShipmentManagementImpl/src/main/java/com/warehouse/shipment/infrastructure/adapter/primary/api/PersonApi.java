package com.warehouse.shipment.infrastructure.adapter.primary.api;

import com.warehouse.commonassets.enumeration.CountryCode;

public record PersonApi(String firstName, String lastName, String email, String telephoneNumber,
                        String city, String postalCode, String street, CountryCode countryCode) {
}
