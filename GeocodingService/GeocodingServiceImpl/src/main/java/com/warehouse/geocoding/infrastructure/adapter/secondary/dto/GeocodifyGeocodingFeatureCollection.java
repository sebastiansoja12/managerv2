package com.warehouse.geocoding.infrastructure.adapter.secondary.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeocodifyGeocodingFeatureCollection(@JsonProperty("type") String type,
                                                  @JsonProperty("features") List<GeocodifyGeocodingFeature> features,
                                                  @JsonProperty("bbox") List<Double> bbox) {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public GeocodifyGeocodingFeatureCollection {
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static GeocodifyGeocodingFeatureCollection empty(final List<Object> emptyResponse) {
        return new GeocodifyGeocodingFeatureCollection(null, List.of(), null);
    }
}
