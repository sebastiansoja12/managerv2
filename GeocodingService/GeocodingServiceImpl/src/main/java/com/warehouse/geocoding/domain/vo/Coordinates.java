package com.warehouse.geocoding.domain.vo;

import lombok.Builder;

@Builder
public record Coordinates(Double lat, Double lon) {
}
