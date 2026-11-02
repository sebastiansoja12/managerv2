package com.warehouse.geocoding.infrastructure.adapter.secondary.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity(name = "geocoding.GeocodingAddressEntity")
@Table(name = "geocoding_addresses", uniqueConstraints = @UniqueConstraint(name = "uk_geocoding_addresses_address",
        columnNames = {"country_code", "postal_code", "city", "street", "house_number"}))
public class GeocodingAddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country_code", nullable = false, length = 2)
    private String countryCode;

    @Column(name = "postal_code", nullable = false, length = 20)
    private String postalCode;

    @Column(name = "city", nullable = false, length = 180)
    private String city;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "house_number", nullable = false, length = 40)
    private String houseNumber;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "provider", length = 40)
    private String provider;

    @Column(name = "geocoded_at", nullable = false)
    private Instant geocodedAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public GeocodingAddressEntity() {
    }

    public Long getId() {
        return id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getProvider() {
        return provider;
    }

    public Instant getGeocodedAt() {
        return geocodedAt;
    }
}
