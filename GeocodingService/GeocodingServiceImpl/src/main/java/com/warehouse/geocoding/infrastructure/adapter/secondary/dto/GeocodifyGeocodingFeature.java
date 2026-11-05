package com.warehouse.geocoding.infrastructure.adapter.secondary.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeocodifyGeocodingFeature(@JsonProperty("type") String type,
                                        @JsonProperty("geometry") GeocodifyGeocodingGeometry geometry,
                                        @JsonProperty("properties") GeocodifyGeocodingProperties properties) {
}
