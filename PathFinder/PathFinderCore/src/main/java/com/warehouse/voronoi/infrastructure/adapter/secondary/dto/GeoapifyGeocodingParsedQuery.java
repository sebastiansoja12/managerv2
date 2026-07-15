package com.warehouse.voronoi.infrastructure.adapter.secondary.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeoapifyGeocodingParsedQuery(
        @JsonProperty("housenumber") String housenumber,
        @JsonProperty("street") String street,
        @JsonProperty("postcode") String postcode,
        @JsonProperty("city") String city,
        @JsonProperty("expected_type") String expectedType) {
}
