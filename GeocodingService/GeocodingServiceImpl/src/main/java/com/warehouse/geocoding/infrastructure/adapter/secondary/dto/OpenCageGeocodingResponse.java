package com.warehouse.geocoding.infrastructure.adapter.secondary.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenCageGeocodingResponse(@JsonProperty("results") List<OpenCageGeocodingResult> results,
                                        @JsonProperty("status") OpenCageGeocodingStatus status,
                                        @JsonProperty("total_results") Integer totalResults) {
}
