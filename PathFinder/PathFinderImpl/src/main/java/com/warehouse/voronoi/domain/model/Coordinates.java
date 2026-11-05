package com.warehouse.voronoi.domain.model;

import lombok.Builder;

@Builder
public record Coordinates(Double lat, Double lon) {
}
