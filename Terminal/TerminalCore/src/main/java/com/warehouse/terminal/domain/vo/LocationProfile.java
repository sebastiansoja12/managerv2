package com.warehouse.terminal.domain.vo;

import com.warehouse.terminal.domain.model.Department;

import java.time.Instant;

public class LocationProfile {
    private Double latitude;
    private Double longitude;
    private Double altitude;
    private Float accuracyMeters;
    private String lastKnownAddress;
    private String geoZone;
    private Boolean gpsEnabled;
    private Instant lastLocationUpdateAt;

    public LocationProfile() {}

    public LocationProfile(
            final Double latitude,
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

    public static LocationProfile initializeLocation(final Department.Coordinates coordinates,
			final String lastKnownAddress, final String geoZone, final boolean gpsEnabled) {
        return new LocationProfile(coordinates.getLatitude(), coordinates.getLongitude(), null, null, lastKnownAddress, geoZone, gpsEnabled, Instant.now());
    }

    public Float getAccuracyMeters() {
        return accuracyMeters;
    }

    public Double getAltitude() {
        return altitude;
    }

    public String getGeoZone() {
        return geoZone;
    }

    public Boolean getGpsEnabled() {
        return gpsEnabled;
    }

    public String getLastKnownAddress() {
        return lastKnownAddress;
    }

    public Instant getLastLocationUpdateAt() {
        return lastLocationUpdateAt;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}

