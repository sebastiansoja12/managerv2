package com.warehouse.infrastructure.dto;

import com.warehouse.commonassets.enumeration.GeocodingProvider;
import com.warehouse.commonassets.identificator.GeocodingConfigurationId;

public record GeocodingConfigurationDto(GeocodingConfigurationId geocodingConfigurationId,
                                        String apiUrl,
                                        String apiUserName,
                                        String apiPassword,
                                        String apiKey,
                                        String accessToken,
                                        String refreshToken,
                                        boolean enabled,
                                        GeocodingProvider provider) {
}
