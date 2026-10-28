package com.warehouse.geocoding.infrastructure.adapter.secondary.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeocodifyGeocodingProperties(
        @JsonProperty("layer") String layer,
        @JsonProperty("source") String source,
        @JsonProperty("name") String name,
        @JsonProperty("housenumber") String housenumber,
        @JsonProperty("street") String street,
        @JsonProperty("postalcode") String postalcode,
        @JsonProperty("confidence") Double confidence,
        @JsonProperty("match_type") String matchType,
        @JsonProperty("accuracy") String accuracy,
        @JsonProperty("country") String country,
        @JsonProperty("country_code") String countryCode,
        @JsonProperty("region") String region,
        @JsonProperty("locality") String locality,
        @JsonProperty("label") String label) {
}
