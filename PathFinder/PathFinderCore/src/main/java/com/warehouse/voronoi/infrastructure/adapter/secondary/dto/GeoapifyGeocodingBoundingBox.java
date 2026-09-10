package com.warehouse.voronoi.infrastructure.adapter.secondary.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeoapifyGeocodingBoundingBox(@JsonProperty("lon1") Double lon1,
                                           @JsonProperty("lat1") Double lat1,
                                           @JsonProperty("lon2") Double lon2,
                                           @JsonProperty("lat2") Double lat2) {
}
