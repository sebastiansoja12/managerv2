package com.warehouse.tracking.infrastructure.adapter.secondary;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.commonassets.security.CredentialCipher;
import com.warehouse.tracking.domain.enumeration.TrackingEnvironment;
import com.warehouse.tracking.domain.model.TrackingIntegrationConfiguration;
import com.warehouse.tracking.infrastructure.adapter.secondary.entity.TrackingIntegrationConfigurationEntity;

public final class TrackingIntegrationConfigurationMapper {

    private TrackingIntegrationConfigurationMapper() {
    }

    public static TrackingIntegrationConfigurationEntity toEntity(
            final TrackingIntegrationConfiguration configuration,
            final CredentialCipher credentialCipher,
            final ObjectMapper objectMapper) {
        final String encryptedConfiguration = credentialCipher.encrypt(writeValues(configuration, objectMapper));
        final TrackingEnvironment legacyEnvironment = configuration.hasValue("environment")
                ? TrackingEnvironment.valueOf(configuration.getValue("environment"))
                : null;
        final String legacySecret = configuration.getValue("clientSecret");
        final TrackingIntegrationConfigurationEntity entity = new TrackingIntegrationConfigurationEntity(
                configuration.getConfigurationId(),
                configuration.getProvider(),
                legacyEnvironment,
                configuration.isEnabled(),
                configuration.getValue("clientId"),
                legacySecret == null ? null : credentialCipher.encrypt(legacySecret),
                encryptedConfiguration);
        entity.assignOperator(configuration.operatorId());
        return entity;
    }

    public static TrackingIntegrationConfiguration toModel(
            final TrackingIntegrationConfigurationEntity entity,
            final CredentialCipher credentialCipher,
            final ObjectMapper objectMapper) {
        final TrackingIntegrationConfiguration configuration = new TrackingIntegrationConfiguration(
                entity.getConfigurationId(),
                entity.getProvider(),
                entity.isEnabled(),
                readValues(entity, credentialCipher, objectMapper));
        configuration.assignOperator(entity.operatorId());
        return configuration;
    }

    private static String writeValues(final TrackingIntegrationConfiguration configuration,
                                      final ObjectMapper objectMapper) {
        try {
            return objectMapper.writeValueAsString(configuration.getValues());
        } catch (final JsonProcessingException exception) {
            throw new IllegalStateException("Could not serialize tracking integration configuration", exception);
        }
    }

    private static Map<String, String> readValues(
            final TrackingIntegrationConfigurationEntity entity,
            final CredentialCipher credentialCipher,
            final ObjectMapper objectMapper) {
        if (entity.getEncryptedConfiguration() == null || entity.getEncryptedConfiguration().isBlank()) {
            final Map<String, String> legacyValues = new LinkedHashMap<>();
            final TrackingEnvironment environment = entity.getEnvironment();
            if (environment != null) {
                legacyValues.put("environment", environment.name());
            }
            if (entity.getClientId() != null && !entity.getClientId().isBlank()) {
                legacyValues.put("clientId", entity.getClientId());
            }
            if (entity.getEncryptedClientSecret() != null && !entity.getEncryptedClientSecret().isBlank()) {
                legacyValues.put("clientSecret", credentialCipher.decrypt(entity.getEncryptedClientSecret()));
            }
            return Map.copyOf(legacyValues);
        }
        try {
            return objectMapper.readValue(
                    credentialCipher.decrypt(entity.getEncryptedConfiguration()),
                    new TypeReference<Map<String, String>>() { });
        } catch (final JsonProcessingException exception) {
            throw new IllegalStateException("Could not deserialize tracking integration configuration", exception);
        }
    }
}
