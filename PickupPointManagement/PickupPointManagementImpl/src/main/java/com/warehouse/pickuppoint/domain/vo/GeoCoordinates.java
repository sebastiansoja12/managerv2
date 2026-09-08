package com.warehouse.pickuppoint.domain.vo;

import com.warehouse.pickuppoint.domain.exception.InvalidPickupPointConfigurationException;

public record GeoCoordinates(double latitude, double longitude) {

    public GeoCoordinates {
        if (!Double.isFinite(latitude) || latitude < -90.0 || latitude > 90.0) {
            throw new InvalidPickupPointConfigurationException("Latitude must be between -90 and 90");
        }
        if (!Double.isFinite(longitude) || longitude < -180.0 || longitude > 180.0) {
            throw new InvalidPickupPointConfigurationException("Longitude must be between -180 and 180");
        }
    }
}
