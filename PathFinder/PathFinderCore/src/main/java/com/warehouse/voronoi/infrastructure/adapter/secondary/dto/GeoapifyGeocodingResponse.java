package com.warehouse.voronoi.infrastructure.adapter.secondary.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeoapifyGeocodingResponse(@JsonProperty("results") List<GeoapifyGeocodingResult> results,
                                        @JsonProperty("features") List<GeoapifyGeocodingFeature> features,
                                        @JsonProperty("statusCode") Integer statusCode,
                                        @JsonProperty("error") String error,
                                        @JsonProperty("message") String message,
                                        @JsonProperty("query") GeoapifyGeocodingQuery query) {
}
