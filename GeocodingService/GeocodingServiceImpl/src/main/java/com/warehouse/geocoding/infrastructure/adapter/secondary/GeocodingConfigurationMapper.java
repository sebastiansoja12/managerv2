package com.warehouse.geocoding.infrastructure.adapter.secondary;

import com.warehouse.commonassets.security.CredentialCipher;
import com.warehouse.geocoding.domain.model.GeocodingConfiguration;
import com.warehouse.geocoding.infrastructure.adapter.secondary.entity.GeocodingConfigurationEntity;

public final class GeocodingConfigurationMapper {

    private GeocodingConfigurationMapper() {
    }

    public static GeocodingConfigurationEntity toEntity(final GeocodingConfiguration configuration,
                                                         final CredentialCipher credentialCipher) {
        final GeocodingConfigurationEntity entity = new GeocodingConfigurationEntity(
                configuration.getGeocodingConfigurationId(),
                configuration.getApiUrl(),
                configuration.getApiUserName(),
                credentialCipher.encrypt(configuration.getApiPassword()),
                credentialCipher.encrypt(configuration.getApiKey()),
                configuration.getClientNumber(),
                credentialCipher.encrypt(configuration.getAccessToken()),
                credentialCipher.encrypt(configuration.getRefreshToken()),
                configuration.isEnabled(),
                configuration.getProvider());
        entity.assignOperator(configuration.operatorId());
        return entity;
    }

    public static GeocodingConfiguration toModel(final GeocodingConfigurationEntity entity,
                                                 final CredentialCipher credentialCipher) {
        final GeocodingConfiguration configuration = new GeocodingConfiguration(
                entity.getGeocodingConfigurationId(),
                entity.getApiUrl(),
                entity.getApiUserName(),
                credentialCipher.decrypt(entity.getApiPassword()),
                credentialCipher.decrypt(entity.getApiKey()),
                entity.getClientNumber(),
                credentialCipher.decrypt(entity.getAccessToken()),
                credentialCipher.decrypt(entity.getRefreshToken()),
                entity.isEnabled(),
                entity.getProvider());
        configuration.assignOperator(entity.operatorId());
        return configuration;
    }
}
