package com.warehouse.geocoding.domain.vo;

import lombok.Builder;

@Builder
public record Coordinates(Double lat, Double lon, boolean approximate) {

    public Coordinates(final Double lat, final Double lon) {
        this(lat, lon, false);
    }
}
