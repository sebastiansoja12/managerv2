package com.warehouse.organisationstructure.api.dto;

import com.warehouse.commonassets.enumeration.GeocodingProvider;

public record OperatorGeocodingConfigurationDto(String apiUserName,
                                                String apiPassword,
                                                String apiKey,
                                                String clientNumber,
                                                String accessToken,
                                                String refreshToken,
                                                boolean enabled,
                                                GeocodingProvider provider) {
}
