package com.warehouse.voronoi.dto;

import com.warehouse.commonassets.enumeration.CountryCode;

public record AddressDto(String city, String street, String postalCode, CountryCode countryCode) {
}
