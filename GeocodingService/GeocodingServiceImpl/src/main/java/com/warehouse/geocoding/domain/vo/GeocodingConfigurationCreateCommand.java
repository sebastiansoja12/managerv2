package com.warehouse.geocoding.domain.vo;

import com.warehouse.commonassets.enumeration.GeocodingProvider;

public record GeocodingConfigurationCreateCommand(String apiUserName,
                                                  String apiPassword,
                                                  String apiKey,
                                                  String accessToken,
                                                  String refreshToken,
                                                  boolean enabled,
                                                  GeocodingProvider provider) {
}
