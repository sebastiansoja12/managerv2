package com.warehouse.infrastructure.dto;

import com.warehouse.commonassets.enumeration.GeocodingProvider;

public record GeocodingConfigurationCreateDto(String apiUserName,
                                              String apiPassword,
                                              String apiKey,
                                              String clientNumber,
                                              String accessToken,
                                              String refreshToken,
                                              boolean enabled,
                                              GeocodingProvider provider) {
}
