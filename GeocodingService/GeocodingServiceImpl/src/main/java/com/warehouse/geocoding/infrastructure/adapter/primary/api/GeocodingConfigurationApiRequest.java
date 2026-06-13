package com.warehouse.geocoding.infrastructure.adapter.primary.api;

import com.warehouse.commonassets.enumeration.GeocodingProvider;

import jakarta.validation.constraints.NotNull;

public record GeocodingConfigurationApiRequest(String apiUserName,
                                               String apiPassword,
                                               String apiKey,
                                               String clientNumber,
                                               String accessToken,
                                               String refreshToken,
                                               boolean enabled,
                                               boolean defaultProvider,
                                               @NotNull GeocodingProvider provider) {
}
