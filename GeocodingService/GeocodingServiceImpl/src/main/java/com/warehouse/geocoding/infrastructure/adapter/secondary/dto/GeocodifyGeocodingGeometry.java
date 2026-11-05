package com.warehouse.geocoding.infrastructure.adapter.secondary.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeocodifyGeocodingGeometry(@JsonProperty("type") String type,
                                         @JsonProperty("coordinates") List<Double> coordinates) {
}
