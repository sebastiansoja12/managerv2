package com.warehouse.geocoding.infrastructure.adapter.secondary.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeocodifyGeocodingMeta(@JsonProperty("code") Integer code,
                                     @JsonProperty("error_type") String errorType,
                                     @JsonProperty("error_detail") String errorDetail) {
}
