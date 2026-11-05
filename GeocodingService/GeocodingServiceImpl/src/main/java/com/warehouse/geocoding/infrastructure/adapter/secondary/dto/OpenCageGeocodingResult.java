package com.warehouse.geocoding.infrastructure.adapter.secondary.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenCageGeocodingResult(@JsonProperty("confidence") Integer confidence,
                                      @JsonProperty("formatted") String formatted,
                                      @JsonProperty("geometry") OpenCageGeocodingGeometry geometry) {
}
