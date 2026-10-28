package com.warehouse.geocoding.infrastructure.adapter.secondary.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeocodifyGeocodingResponse(@JsonProperty("meta") GeocodifyGeocodingMeta meta,
                                         @JsonProperty("response") GeocodifyGeocodingFeatureCollection response) {
}
