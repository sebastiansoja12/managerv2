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
                encryptCredential(configuration.getApiPassword(), credentialCipher),
                encryptCredential(configuration.getApiKey(), credentialCipher),
                configuration.getClientNumber(),
                encryptCredential(configuration.getAccessToken(), credentialCipher),
                encryptCredential(configuration.getRefreshToken(), credentialCipher),
                configuration.isEnabled(),
                configuration.isDefaultProvider(),
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
                decryptCredential(entity.getApiPassword(), credentialCipher),
                decryptCredential(entity.getApiKey(), credentialCipher),
                entity.getClientNumber(),
                decryptCredential(entity.getAccessToken(), credentialCipher),
                decryptCredential(entity.getRefreshToken(), credentialCipher),
                entity.isEnabled(),
                entity.isDefaultProvider(),
                entity.getProvider());
        configuration.assignOperator(entity.operatorId());
        return configuration;
    }

    private static String encryptCredential(final String credential,
                                            final CredentialCipher credentialCipher) {
        if (credential == null) {
            return null;
        }
        return credentialCipher.encrypt(credential);
    }

    private static String decryptCredential(final String credential,
                                            final CredentialCipher credentialCipher) {
        if (credential == null) {
            return null;
        }
        try {
            return credentialCipher.decrypt(credential);
        } catch (final IllegalStateException exception) {
            return credential;
        }
    }
}
