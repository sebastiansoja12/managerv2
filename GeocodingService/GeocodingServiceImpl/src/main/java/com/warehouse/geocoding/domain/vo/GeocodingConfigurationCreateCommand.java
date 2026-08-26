package com.warehouse.geocoding.domain.vo;

import com.warehouse.commonassets.enumeration.GeocodingProvider;

public record GeocodingConfigurationCreateCommand(String apiUserName,
                                                  String apiPassword,
                                                  String apiKey,
                                                  String clientNumber,
                                                  String accessToken,
                                                  String refreshToken,
                                                  boolean enabled,
                                                  boolean defaultProvider,
                                                  GeocodingProvider provider) {
}
