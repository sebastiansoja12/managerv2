package com.warehouse.tracking.domain.service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.warehouse.tracking.domain.enumeration.TrackingProviderId;
import com.warehouse.tracking.domain.exception.TrackingErrorCode;
import com.warehouse.tracking.domain.exception.TrackingException;
import com.warehouse.tracking.domain.model.TrackingIntegrationConfiguration;
import com.warehouse.tracking.domain.model.TrackingIntegrationDefinition;
import com.warehouse.tracking.domain.port.secondary.TrackingIntegrationRepository;
import com.warehouse.tracking.domain.port.secondary.TrackingTokenServicePort;

public class TrackingIntegrationService {

    private final TrackingIntegrationRepository repository;
    private final TrackingTokenServicePort tokenServicePort;
    private final TrackingProviderRegistry providerRegistry;

    public TrackingIntegrationService(final TrackingIntegrationRepository repository,
                                      final TrackingTokenServicePort tokenServicePort,
                                      final TrackingProviderRegistry providerRegistry) {
        this.repository = repository;
        this.tokenServicePort = tokenServicePort;
        this.providerRegistry = providerRegistry;
    }

    public Optional<TrackingIntegrationConfiguration> find(final TrackingProviderId providerId) {
        return repository.findByProvider(providerId);
    }

    public TrackingIntegrationConfiguration getRequired(final TrackingProviderId providerId) {
        return find(providerId).orElseThrow(() -> new TrackingException(
                TrackingErrorCode.CONFIGURATION_ERROR, 409,
                "Tracking provider configuration is missing", null));
    }

    public void save(final TrackingProviderId providerId,
                     final boolean enabled,
                     final Map<String, String> values) {
        final Optional<TrackingIntegrationConfiguration> existing = repository.findByProvider(providerId);
        final TrackingIntegrationDefinition definition = providerRegistry.integrationDefinition(providerId);
        final Map<String, String> storedValues = existing
                .map(TrackingIntegrationConfiguration::getValues)
                .orElseGet(Map::of);
        final Map<String, String> configurationValues = definition.resolveValues(values, storedValues);
        if (existing.isEmpty()) {
            final TrackingIntegrationConfiguration configuration = new TrackingIntegrationConfiguration(
                    UUID.randomUUID(), providerId, enabled, configurationValues);
            repository.create(configuration);
            tokenServicePort.invalidate(configuration.getConfigurationId());
            return;
        }

        final TrackingIntegrationConfiguration configuration = existing.orElseThrow();
        configuration.update(enabled, configurationValues);
        repository.update(configuration);
        tokenServicePort.invalidate(configuration.getConfigurationId());
    }

    public void testConnection(final TrackingProviderId providerId, final Map<String, String> values) {
        final Map<String, String> storedValues = repository.findByProvider(providerId)
                .map(TrackingIntegrationConfiguration::getValues)
                .orElseGet(Map::of);
        final Map<String, String> configurationValues = providerRegistry.integrationDefinition(providerId)
                .resolveValues(values, storedValues);
        final TrackingIntegrationConfiguration testConfiguration = new TrackingIntegrationConfiguration(
                UUID.randomUUID(), providerId, true, configurationValues);
        providerRegistry.testConnection(providerId, testConfiguration);
    }
}
