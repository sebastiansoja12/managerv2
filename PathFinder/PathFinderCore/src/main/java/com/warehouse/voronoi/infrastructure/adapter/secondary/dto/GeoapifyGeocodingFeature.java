package com.warehouse.voronoi.infrastructure.adapter.secondary.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeoapifyGeocodingFeature(@JsonProperty("properties") GeoapifyGeocodingResult properties,
                                       @JsonProperty("geometry") GeoapifyGeocodingGeometry geometry) {
}
