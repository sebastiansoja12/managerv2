package com.warehouse.terminal.domain.vo;

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

	public LocationProfile(final Float accuracyMeters, final Double altitude, final String geoZone,
			final Boolean gpsEnabled, final String lastKnownAddress, final Instant lastLocationUpdateAt,
			final Double latitude, final Double longitude) {
        this.accuracyMeters = accuracyMeters;
        this.altitude = altitude;
        this.geoZone = geoZone;
        this.gpsEnabled = gpsEnabled;
        this.lastKnownAddress = lastKnownAddress;
        this.lastLocationUpdateAt = lastLocationUpdateAt;
        this.latitude = latitude;
        this.longitude = longitude;
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

