package com.warehouse.shipment.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.enumeration.CountryCode;

import jakarta.persistence.*;

@Table(name = "country")
@Entity
public class CountryEntity {

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "country_code", nullable = false, unique = true, length = 50)
    private CountryCode countryCode;

    @Column(name = "name", nullable = false)
    private String name;

    public CountryEntity() {
    }

    public CountryEntity(final CountryCode countryCode, final String name) {
        this.countryCode = countryCode;
        this.name = name;
    }

    public CountryCode getCountryCode() {
        return countryCode;
    }

    public String getName() {
        return name;
    }
}
