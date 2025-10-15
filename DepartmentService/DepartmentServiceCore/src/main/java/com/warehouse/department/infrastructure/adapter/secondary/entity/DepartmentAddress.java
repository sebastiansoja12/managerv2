package com.warehouse.department.infrastructure.adapter.secondary.entity;


import com.warehouse.commonassets.enumeration.Country;

public class DepartmentAddress {
    private final String city;
    private final String street;
    private final Country country;
    private final String postalCode;

    public DepartmentAddress(final String city,
                             final Country country,
                             final String postalCode,
                             final String street) {
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public Country getCountry() {
        return country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getStreet() {
        return street;
    }
}
