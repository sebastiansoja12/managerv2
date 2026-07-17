package com.warehouse.voronoi.domain.vo;

import com.warehouse.commonassets.enumeration.GeocodingProvider;
import com.warehouse.infrastructure.dto.GeocodingConfigurationDto;

public record GeocodingConfig(GeocodingProvider provider, String apiKey, String baseUrl, String username, String password) {
    public static GeocodingConfig from(final GeocodingConfigurationDto geocodingConfig) {
        if (geocodingConfig == null) {
            return null;
        }
        return new GeocodingConfig(geocodingConfig.provider(), geocodingConfig.apiKey(), geocodingConfig.apiUrl(), geocodingConfig.apiUserName(), geocodingConfig.apiPassword());
    }
}
