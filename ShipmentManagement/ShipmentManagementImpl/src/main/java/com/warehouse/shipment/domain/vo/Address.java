package com.warehouse.shipment.domain.vo;

import com.warehouse.commonassets.enumeration.CountryCode;

public class Address {
    private final String city;
    private final String street;
    private final String postalCode;
    private final CountryCode countryCode;

    public Address(final String city, final String street, final String postalCode) {
        this(city, street, postalCode, null);
    }

    public Address(final String city, final String street, final String postalCode, final CountryCode countryCode) {
        this.city = city;
        this.street = street;
        this.postalCode = postalCode;
        this.countryCode = countryCode;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public CountryCode getCountryCode() {
        return countryCode;
    }

    public static Address from(final Party party) {
        return party.getAddress();
    }
}
