package com.warehouse.voronoi.infrastructure.adapter.secondary.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeoapifyGeocodingDatasource(@JsonProperty("sourcename") String sourcename,
                                          @JsonProperty("attribution") String attribution,
                                          @JsonProperty("license") String license,
                                          @JsonProperty("url") String url) {
}
