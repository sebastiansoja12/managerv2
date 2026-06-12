package com.warehouse.tracking.infrastructure.adapter.secondary.inpost;

import java.util.List;
import java.util.UUID;

import org.slf4j.MDC;

import com.warehouse.tracking.domain.enumeration.TrackingProviderId;
import com.warehouse.tracking.domain.exception.TrackingErrorCode;
import com.warehouse.tracking.domain.exception.TrackingException;
import com.warehouse.tracking.domain.model.ExternalTrackingResult;
import com.warehouse.tracking.domain.model.TrackingIntegrationConfiguration;
import com.warehouse.tracking.domain.model.TrackingIntegrationDefinition;
import com.warehouse.tracking.domain.model.TrackingIntegrationFieldDefinition;
import com.warehouse.tracking.domain.model.TrackingIntegrationFieldOption;
import com.warehouse.tracking.domain.model.TrackingIntegrationFieldType;
import com.warehouse.tracking.domain.port.secondary.TrackingIntegrationRepository;
import com.warehouse.tracking.domain.port.secondary.TrackingProviderServicePort;
import com.warehouse.tracking.domain.port.secondary.TrackingTokenServicePort;

public class InPostTrackingServiceAdapter implements TrackingProviderServicePort {

    private static final String CORRELATION_ID_MDC_KEY = "correlationId";
    private static final TrackingIntegrationDefinition DEFINITION = new TrackingIntegrationDefinition(
            TrackingProviderId.INPOST,
            TrackingProviderId.INPOST.getDisplayName(),
            List.of(
                    new TrackingIntegrationFieldDefinition(
                            "environment", "Environment", TrackingIntegrationFieldType.SELECT,
                            true, "STAGE", 32, List.of(
                            new TrackingIntegrationFieldOption("STAGE", "Stage"),
                            new TrackingIntegrationFieldOption("PRODUCTION", "Production"))),
                    new TrackingIntegrationFieldDefinition(
                            "clientId", "Client ID", TrackingIntegrationFieldType.TEXT,
                            true, "", 512, List.of()),
                    new TrackingIntegrationFieldDefinition(
                            "clientSecret", "Client secret", TrackingIntegrationFieldType.SECRET,
                            true, "", 1024, List.of())));

    private final TrackingIntegrationRepository configurationRepository;
    private final TrackingTokenServicePort tokenServicePort;
    private final InPostTrackingClient trackingClient;

    public InPostTrackingServiceAdapter(final TrackingIntegrationRepository configurationRepository,
                                        final TrackingTokenServicePort tokenServicePort,
                                        final InPostTrackingClient trackingClient) {
        this.configurationRepository = configurationRepository;
        this.tokenServicePort = tokenServicePort;
        this.trackingClient = trackingClient;
    }

    @Override
    public TrackingProviderId providerId() {
        return TrackingProviderId.INPOST;
    }

    @Override
    public TrackingIntegrationDefinition integrationDefinition() {
        return DEFINITION;
    }

    @Override
    public boolean isAvailable() {
        return configurationRepository.findByProvider(providerId())
                .filter(TrackingIntegrationConfiguration::isEnabled)
                .filter(configuration -> configuration.isConfigured(DEFINITION))
                .isPresent();
    }

    @Override
    public List<ExternalTrackingResult> track(final List<String> trackingNumbers) {
        final TrackingIntegrationConfiguration configuration = requiredConfiguration(true);
        final String requestId = requestId();
        final String accessToken = tokenServicePort.accessToken(configuration, requestId);
        final List<ExternalTrackingResult> results = trackingClient.track(
                        configuration, trackingNumbers, accessToken, requestId)
                .parcels()
                .stream()
                .map(InPostTrackingMapper::toModel)
                .toList();
        if (results.isEmpty()) {
            throw new TrackingException(TrackingErrorCode.NOT_FOUND, 404,
                    "The parcel was not found in InPost", requestId);
        }
        return results;
    }

    @Override
    public void testConnection(final TrackingIntegrationConfiguration configuration) {
        if (!configuration.isConfigured(DEFINITION)) {
            throw new TrackingException(TrackingErrorCode.CONFIGURATION_ERROR, 400,
                    "InPost integration is not configured", null);
        }
        try {
            tokenServicePort.accessToken(configuration, requestId());
        } finally {
            tokenServicePort.invalidate(configuration.getConfigurationId());
        }
    }

    private TrackingIntegrationConfiguration requiredConfiguration(final boolean requireEnabled) {
        final TrackingIntegrationConfiguration configuration = configurationRepository.findByProvider(providerId())
                .orElseThrow(() -> new TrackingException(TrackingErrorCode.CONFIGURATION_ERROR, 409,
                        "InPost configuration is missing", null));
        if (!configuration.isConfigured(DEFINITION) || (requireEnabled && !configuration.isEnabled())) {
            throw new TrackingException(TrackingErrorCode.CONFIGURATION_ERROR, 409,
                    "InPost integration is disabled or not configured", null);
        }
        return configuration;
    }

    private String requestId() {
        final String correlationId = MDC.get(CORRELATION_ID_MDC_KEY);
        return correlationId != null && !correlationId.isBlank() ? correlationId : UUID.randomUUID().toString();
    }
}
