package com.warehouse.geocoding.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.identificator.GeocodingConfigurationId;
import com.warehouse.geocoding.domain.model.GeocodingConfiguration;
import com.warehouse.geocoding.domain.vo.GeocodingConfigurationCreateCommand;
import com.warehouse.geocoding.domain.vo.GeocodingConfigurationUpdateCommand;
import com.warehouse.geocoding.infrastructure.adapter.primary.api.GeocodingConfigurationApiRequest;
import com.warehouse.geocoding.infrastructure.adapter.primary.api.GeocodingConfigurationApiResponse;
import com.warehouse.infrastructure.dto.GeocodingConfigurationDto;

public final class GeocodingConfigurationApiMapper {

    private GeocodingConfigurationApiMapper() {
    }

    public static GeocodingConfigurationCreateCommand toCreateCommand(
            final GeocodingConfigurationApiRequest request) {
        return new GeocodingConfigurationCreateCommand(
                request.apiUserName(),
                request.apiPassword(),
                request.apiKey(),
                request.accessToken(),
                request.refreshToken(),
                request.enabled(),
                request.provider());
    }

    public static GeocodingConfigurationUpdateCommand toUpdateCommand(
            final GeocodingConfigurationId geocodingConfigurationId,
            final GeocodingConfigurationApiRequest request) {
        return new GeocodingConfigurationUpdateCommand(
                geocodingConfigurationId,
                request.apiUserName(),
                request.apiPassword(),
                request.apiKey(),
                request.accessToken(),
                request.refreshToken(),
                request.enabled(),
                request.provider());
    }

    public static GeocodingConfigurationApiResponse toResponse(
            final GeocodingConfiguration configuration) {
        return new GeocodingConfigurationApiResponse(
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

    public static GeocodingConfigurationDto toDto(final GeocodingConfiguration configuration) {
        return new GeocodingConfigurationDto(
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
