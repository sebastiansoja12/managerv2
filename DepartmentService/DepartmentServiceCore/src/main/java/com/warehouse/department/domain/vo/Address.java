package com.warehouse.department.domain.vo;

import com.warehouse.commonassets.enumeration.CountryCode;

public record Address(String city,
                      String street,
                      String country,
                      String postalCode,
                      CountryCode countryCode) {


    public void validate() {
        if (city == null || street == null || country == null || postalCode == null || countryCode == null) {
            throw new IllegalArgumentException("Address cannot be null");
        }
    }
}
