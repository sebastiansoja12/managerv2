package com.warehouse.geocoding.domain.vo;

import com.warehouse.commonassets.enumeration.CountryCode;

public record PostalCode(CountryCode countryCode, String postalCode, String city, String regionCode,
                         Coordinates coordinates) {
}
