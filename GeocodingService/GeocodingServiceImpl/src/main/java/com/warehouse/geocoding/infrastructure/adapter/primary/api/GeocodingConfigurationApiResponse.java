package com.warehouse.geocoding.infrastructure.adapter.primary.api;

import com.warehouse.commonassets.enumeration.GeocodingProvider;
import com.warehouse.commonassets.identificator.GeocodingConfigurationId;

public record GeocodingConfigurationApiResponse(GeocodingConfigurationId geocodingConfigurationId,
                                                String apiUrl,
                                                String apiUserName,
                                                String apiPassword,
                                                String apiKey,
                                                String accessToken,
                                                String refreshToken,
                                                boolean enabled,
                                                GeocodingProvider provider) {
}
