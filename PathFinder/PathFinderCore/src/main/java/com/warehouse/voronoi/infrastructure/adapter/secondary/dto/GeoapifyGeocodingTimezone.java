package com.warehouse.voronoi.infrastructure.adapter.secondary.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeoapifyGeocodingTimezone(
        @JsonProperty("name") String name,
        @JsonProperty("offset_STD") String offsetStd,
        @JsonProperty("offset_DST") String offsetDst,
        @JsonProperty("offset_STD_seconds") Integer offsetStdSeconds,
        @JsonProperty("offset_DST_seconds") Integer offsetDstSeconds,
        @JsonProperty("abbreviation_STD") String abbreviationStd,
        @JsonProperty("abbreviation_DST") String abbreviationDst) {
}
