package com.warehouse.deliverynetwork.domain.vo;

public record DepartmentLocation(double latitude, double longitude) {

    private static final double EARTH_RADIUS_IN_KILOMETERS = 6371.0;

    public double distanceInKilometersTo(final DepartmentLocation location) {
        final double latitudeDelta = Math.toRadians(location.latitude() - this.latitude);
        final double longitudeDelta = Math.toRadians(location.longitude() - this.longitude);
        final double haversine = Math.pow(Math.sin(latitudeDelta / 2), 2)
                + Math.cos(Math.toRadians(this.latitude))
                * Math.cos(Math.toRadians(location.latitude()))
                * Math.pow(Math.sin(longitudeDelta / 2), 2);
        return 2 * EARTH_RADIUS_IN_KILOMETERS * Math.asin(Math.sqrt(haversine));
    }
}
