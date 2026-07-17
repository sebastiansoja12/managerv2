package com.warehouse.geocoding.domain.vo;

import com.warehouse.commonassets.enumeration.GeocodingProvider;
import com.warehouse.commonassets.identificator.GeocodingConfigurationId;
import com.warehouse.geocoding.domain.model.GeocodingConfiguration;

public record GeocodingConfigurationResult(GeocodingConfigurationId geocodingConfigurationId,
                                          String apiUrl,
                                          String apiUserName,
                                          String apiPassword,
                                          String apiKey,
                                          String accessToken,
                                          String refreshToken,
                                          boolean enabled,
                                          GeocodingProvider provider) {

    public static GeocodingConfigurationResult from(final GeocodingConfiguration configuration) {
        return new GeocodingConfigurationResult(
                configuration.getGeocodingConfigurationId(),
                configuration.getApiUrl(),
                configuration.getApiUserName(),
                configuration.getApiPassword(),
                configuration.getApiKey(),
                configuration.getAccessToken(),
                configuration.getRefreshToken(),
                configuration.isEnabled(),
                configuration.getProvider());
    }
}
