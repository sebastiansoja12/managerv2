package com.warehouse.geocoding.domain.vo;

import com.warehouse.commonassets.enumeration.GeocodingProvider;
import com.warehouse.commonassets.identificator.GeocodingConfigurationId;

public record GeocodingConfigurationUpdateCommand(GeocodingConfigurationId geocodingConfigurationId,
                                                  String apiUserName,
                                                  String apiPassword,
                                                  String apiKey,
                                                  String accessToken,
                                                  String refreshToken,
                                                  boolean enabled,
                                                  GeocodingProvider provider) {
}
