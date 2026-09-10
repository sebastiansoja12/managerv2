package com.warehouse.voronoi.infrastructure.adapter.secondary.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeoapifyGeocodingResult(
        @JsonProperty("datasource") GeoapifyGeocodingDatasource datasource,
        @JsonProperty("country") String country,
        @JsonProperty("country_code") String countryCode,
        @JsonProperty("state") String state,
        @JsonProperty("city") String city,
        @JsonProperty("postcode") String postcode,
        @JsonProperty("district") String district,
        @JsonProperty("suburb") String suburb,
        @JsonProperty("street") String street,
        @JsonProperty("housenumber") String housenumber,
        @JsonProperty("iso3166_2") String iso31662,
        @JsonProperty("lon") Double lon,
        @JsonProperty("lat") Double lat,
        @JsonProperty("state_code") String stateCode,
        @JsonProperty("result_type") String resultType,
        @JsonProperty("formatted") String formatted,
        @JsonProperty("address_line1") String addressLine1,
        @JsonProperty("address_line2") String addressLine2,
        @JsonProperty("timezone") GeoapifyGeocodingTimezone timezone,
        @JsonProperty("plus_code") String plusCode,
        @JsonProperty("rank") GeoapifyGeocodingRank rank,
        @JsonProperty("place_id") String placeId,
        @JsonProperty("bbox") GeoapifyGeocodingBoundingBox bbox) {
}
