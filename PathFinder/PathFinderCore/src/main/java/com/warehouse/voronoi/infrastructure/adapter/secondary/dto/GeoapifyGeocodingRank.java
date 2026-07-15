package com.warehouse.voronoi.infrastructure.adapter.secondary.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeoapifyGeocodingRank(
        @JsonProperty("importance") Double importance,
        @JsonProperty("popularity") Double popularity,
        @JsonProperty("confidence") Double confidence,
        @JsonProperty("confidence_city_level") Double confidenceCityLevel,
        @JsonProperty("confidence_street_level") Double confidenceStreetLevel,
        @JsonProperty("confidence_building_level") Double confidenceBuildingLevel,
        @JsonProperty("match_type") String matchType) {
}
