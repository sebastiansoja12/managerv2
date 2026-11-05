package com.warehouse.geocoding.infrastructure.adapter.secondary.entity;

import java.io.Serializable;
import java.util.Objects;

import com.warehouse.commonassets.enumeration.CountryCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity(name = "geocoding.PostalCodeEntity")
@Table(name = "postal_codes")
@IdClass(PostalCodeEntity.PostalCodeEntityId.class)
public class PostalCodeEntity {

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "country_code", nullable = false, length = 2)
    private CountryCode countryCode;

    @Id
    @Column(name = "postal_code", nullable = false, length = 20)
    private String postalCode;

    @Id
    @Column(name = "city", nullable = false, length = 180)
    private String city;

    @Column(name = "region_code", length = 10)
    private String regionCode;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    public PostalCodeEntity() {
    }

    public CountryCode getCountryCode() {
        return countryCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public static class PostalCodeEntityId implements Serializable {

        private CountryCode countryCode;

        private String postalCode;

        private String city;

        public PostalCodeEntityId() {
        }

        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof PostalCodeEntityId that)) {
                return false;
            }
            return countryCode == that.countryCode && Objects.equals(postalCode, that.postalCode)
                    && Objects.equals(city, that.city);
        }

        @Override
        public int hashCode() {
            return Objects.hash(countryCode, postalCode, city);
        }
    }
}
