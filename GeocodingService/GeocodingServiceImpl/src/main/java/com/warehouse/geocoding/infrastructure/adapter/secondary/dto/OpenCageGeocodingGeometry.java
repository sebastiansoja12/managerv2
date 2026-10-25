package com.warehouse.geocoding.infrastructure.adapter.secondary.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenCageGeocodingGeometry(@JsonProperty("lat") Double lat,
                                       @JsonProperty("lng") Double lng) {
}
