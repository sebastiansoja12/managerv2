package com.warehouse.geocoding.infrastructure.adapter.primary.api;

import com.warehouse.commonassets.enumeration.GeocodingProvider;

import jakarta.validation.constraints.NotNull;

public record GeocodingConfigurationApiRequest(String apiUserName,
                                               String apiPassword,
                                               String apiKey,
                                               String accessToken,
                                               String refreshToken,
                                               boolean enabled,
                                               @NotNull GeocodingProvider provider) {
}
