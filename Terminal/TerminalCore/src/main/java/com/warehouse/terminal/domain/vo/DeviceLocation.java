package com.warehouse.terminal.domain.vo;

import com.warehouse.terminal.domain.model.Department;

import java.time.Instant;

public class DeviceLocation {
    private Double latitude;
    private Double longitude;
    private Double altitude;
    private Float accuracyMeters;
    private String lastKnownAddress;
    private String geoZone;
    private Boolean gpsEnabled;
    private Instant lastLocationUpdateAt;

    public DeviceLocation() {}

    public DeviceLocation(
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

    public static DeviceLocation initializeLocation(final Department.Coordinates coordinates,
                                                    final String lastKnownAddress, final String geoZone, final boolean gpsEnabled) {
        return new DeviceLocation(coordinates.getLatitude(), coordinates.getLongitude(), null, null, lastKnownAddress, geoZone, gpsEnabled, Instant.now());
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

    public void update(final DeviceLocation location) {
        if (location.getLatitude() != null) {
            this.latitude = location.getLatitude();
        }
        if (location.getLongitude() != null) {
            this.longitude = location.getLongitude();
        }
        if (location.getAltitude() != null) {
            this.altitude = location.getAltitude();
        }
        if (location.getAccuracyMeters() != null) {
            this.accuracyMeters = location.getAccuracyMeters();
        }
        if (location.getLastKnownAddress() != null) {
            this.lastKnownAddress = location.getLastKnownAddress();
        }
        if (location.getGeoZone() != null) {
            this.geoZone = location.getGeoZone();
        }
        if (location.getGpsEnabled() != null) {
            this.gpsEnabled = location.getGpsEnabled();
        }
        if (location.getLastLocationUpdateAt() != null) {
            this.lastLocationUpdateAt = location.getLastLocationUpdateAt();
        }
    }
}
