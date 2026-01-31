package com.warehouse.terminal.infrastructure.adapter.secondary.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Location {

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "altitude")
    private Double altitude;

    @Column(name = "accuracy_meters")
    private Float accuracyMeters;

    @Column(name = "last_known_address")
    private String lastKnownAddress;

    @Column(name = "geo_zone")
    private String geoZone;

    @Column(name = "gps_enabled")
    private Boolean gpsEnabled;

    @Column(name = "last_location_update_at")
    private Instant lastLocationUpdateAt;

    protected Location() {}

    public Location(final Double latitude,
                    final Double longitude,
                    final Double altitude,
                    final Float accuracyMeters,
                    final String lastKnownAddress,
                    final String geoZone,
                    final Boolean gpsEnabled,
                    final Instant lastLocationUpdateAt) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.accuracyMeters = accuracyMeters;
        this.lastKnownAddress = lastKnownAddress;
        this.geoZone = geoZone;
        this.gpsEnabled = gpsEnabled;
        this.lastLocationUpdateAt = lastLocationUpdateAt;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getAltitude() {
        return altitude;
    }

    public Float getAccuracyMeters() {
        return accuracyMeters;
    }

    public String getLastKnownAddress() {
        return lastKnownAddress;
    }

    public String getGeoZone() {
        return geoZone;
    }

    public Boolean getGpsEnabled() {
        return gpsEnabled;
    }

    public Instant getLastLocationUpdateAt() {
        return lastLocationUpdateAt;
    }
}
