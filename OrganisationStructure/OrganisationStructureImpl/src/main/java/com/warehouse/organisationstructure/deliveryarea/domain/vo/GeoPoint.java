package com.warehouse.organisationstructure.deliveryarea.domain.vo;

public record GeoPoint(Double latitude, Double longitude) {

    public boolean isValid() {
        return latitude != null && longitude != null && Double.isFinite(latitude) && Double.isFinite(longitude)
                && Math.abs(latitude) <= 90 && Math.abs(longitude) <= 180;
    }
}
