package com.warehouse.tracking.domain.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.warehouse.tracking.domain.enumeration.TrackingProviderId;
import com.warehouse.tracking.domain.exception.TrackingErrorCode;
import com.warehouse.tracking.domain.exception.TrackingException;
import com.warehouse.tracking.domain.model.ExternalTrackingResult;
import com.warehouse.tracking.domain.model.TrackingIntegrationConfiguration;
import com.warehouse.tracking.domain.model.TrackingIntegrationDefinition;
import com.warehouse.tracking.domain.port.secondary.TrackingProviderServicePort;

public class TrackingProviderRegistry {

    private static final int PROVIDER_BATCH_SIZE = 10;

    private final Map<TrackingProviderId, TrackingProviderServicePort> providers;

    public TrackingProviderRegistry(final List<TrackingProviderServicePort> providers) {
        final Map<TrackingProviderId, TrackingProviderServicePort> registeredProviders = new LinkedHashMap<>();
        providers.forEach(provider -> {
            if (registeredProviders.put(provider.providerId(), provider) != null) {
                throw new IllegalStateException("Duplicate tracking provider " + provider.providerId());
            }
        });
        this.providers = Map.copyOf(registeredProviders);
    }

    public List<TrackingProviderId> availableProviders() {
        return providers.values().stream()
                .filter(TrackingProviderServicePort::isAvailable)
                .map(TrackingProviderServicePort::providerId)
                .toList();
    }

    public List<TrackingProviderId> registeredProviders() {
        return providers.keySet().stream().toList();
    }

    public List<TrackingIntegrationDefinition> integrationDefinitions() {
        return providers.values().stream()
                .map(TrackingProviderServicePort::integrationDefinition)
                .toList();
    }

    public TrackingIntegrationDefinition integrationDefinition(final TrackingProviderId providerId) {
        return provider(providerId).integrationDefinition();
    }

    public List<ExternalTrackingResult> track(final TrackingProviderId providerId,
                                              final List<String> trackingNumbers) {
        final TrackingProviderServicePort provider = provider(providerId);
        if (!provider.isAvailable()) {
            throw new TrackingException(TrackingErrorCode.CONFIGURATION_ERROR, 409,
                    "The selected tracking provider is disabled or not configured", null);
        }

        if (trackingNumbers == null || trackingNumbers.isEmpty()
                || trackingNumbers.stream().anyMatch(number -> number == null || number.isBlank())) {
            throw invalidTrackingNumber();
        }
        final List<String> normalizedTrackingNumbers = trackingNumbers.stream()
                .map(String::trim)
                .distinct()
                .toList();
        if (normalizedTrackingNumbers.isEmpty()
                || normalizedTrackingNumbers.stream().anyMatch(number -> !number.matches("[A-Za-z0-9-]{6,64}"))) {
            throw invalidTrackingNumber();
        }

        final java.util.ArrayList<ExternalTrackingResult> results = new java.util.ArrayList<>();
        for (int index = 0; index < normalizedTrackingNumbers.size(); index += PROVIDER_BATCH_SIZE) {
            final int endIndex = Math.min(index + PROVIDER_BATCH_SIZE, normalizedTrackingNumbers.size());
            results.addAll(provider.track(normalizedTrackingNumbers.subList(index, endIndex)));
        }
        return List.copyOf(results);
    }

    public void testConnection(final TrackingProviderId providerId,
                               final TrackingIntegrationConfiguration configuration) {
        provider(providerId).testConnection(configuration);
    }

    private TrackingProviderServicePort provider(final TrackingProviderId providerId) {
        if (providerId == null) {
            throw new TrackingException(TrackingErrorCode.UNKNOWN_PROVIDER, 400,
                    "Unknown tracking provider", null);
        }
        final TrackingProviderServicePort provider = providers.get(providerId);
        if (provider == null) {
            throw new TrackingException(TrackingErrorCode.UNKNOWN_PROVIDER, 400,
                    "Unknown tracking provider", null);
        }
        return provider;
    }

    private TrackingException invalidTrackingNumber() {
        return new TrackingException(TrackingErrorCode.INVALID_TRACKING_NUMBER, 400,
                "Tracking number must contain 6 to 64 letters, digits or hyphens", null);
    }
}
