package com.warehouse.geocoding.infrastructure.adapter.secondary;

import com.warehouse.geocoding.domain.model.GeocodingConfiguration;
import com.warehouse.geocoding.infrastructure.adapter.secondary.entity.GeocodingConfigurationEntity;

public final class GeocodingConfigurationMapper {

    private GeocodingConfigurationMapper() {
    }

    public static GeocodingConfigurationEntity toEntity(final GeocodingConfiguration configuration,
                                                         final GeocodingPasswordCipher passwordCipher) {
        final GeocodingConfigurationEntity entity = new GeocodingConfigurationEntity(
                configuration.getGeocodingConfigurationId(),
                configuration.getApiUrl(),
                configuration.getApiUserName(),
                passwordCipher.encrypt(configuration.getApiPassword()),
                configuration.getApiKey(),
                configuration.getAccessToken(),
                configuration.getRefreshToken(),
                configuration.isEnabled(),
                configuration.getProvider());
        entity.assignOperator(configuration.operatorId());
        return entity;
    }

    public static GeocodingConfiguration toModel(final GeocodingConfigurationEntity entity,
                                                 final GeocodingPasswordCipher passwordCipher) {
        final GeocodingConfiguration configuration = new GeocodingConfiguration(
                entity.getGeocodingConfigurationId(),
                entity.getApiUrl(),
                entity.getApiUserName(),
                passwordCipher.decrypt(entity.getApiPassword()),
                entity.getApiKey(),
                entity.getAccessToken(),
                entity.getRefreshToken(),
                entity.isEnabled(),
                entity.getProvider());
        configuration.assignOperator(entity.operatorId());
        return configuration;
    }
}
