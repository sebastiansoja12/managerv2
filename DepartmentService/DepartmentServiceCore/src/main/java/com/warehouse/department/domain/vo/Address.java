package com.warehouse.department.domain.vo;

import com.warehouse.commonassets.enumeration.CountryCode;

public record Address(String city,
                      String street,
                      String country,
                      String postalCode,
                      CountryCode countryCode) {
}
