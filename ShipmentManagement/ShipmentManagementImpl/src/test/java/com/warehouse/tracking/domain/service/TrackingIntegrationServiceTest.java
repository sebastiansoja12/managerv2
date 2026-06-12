package com.warehouse.tracking.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.warehouse.tracking.domain.enumeration.TrackingProviderId;
import com.warehouse.tracking.domain.exception.TrackingException;
import com.warehouse.tracking.domain.model.TrackingIntegrationConfiguration;
import com.warehouse.tracking.domain.model.TrackingIntegrationDefinition;
import com.warehouse.tracking.domain.model.TrackingIntegrationFieldDefinition;
import com.warehouse.tracking.domain.model.TrackingIntegrationFieldOption;
import com.warehouse.tracking.domain.model.TrackingIntegrationFieldType;
import com.warehouse.tracking.domain.port.secondary.TrackingIntegrationRepository;
import com.warehouse.tracking.domain.port.secondary.TrackingTokenServicePort;

class TrackingIntegrationServiceTest {

    @Test
    void shouldEncryptAtRepositoryBoundaryAndPreserveAnExistingSecretOnBlankUpdate() {
        final TrackingIntegrationRepository repository = mock(TrackingIntegrationRepository.class);
        final TrackingTokenServicePort tokenService = mock(TrackingTokenServicePort.class);
        final TrackingProviderRegistry providerRegistry = providerRegistry();
        final TrackingIntegrationConfiguration existing = new TrackingIntegrationConfiguration(
                UUID.randomUUID(), TrackingProviderId.INPOST, true,
                values("STAGE", "old-client", "existing-secret"));
        when(repository.findByProvider(TrackingProviderId.INPOST)).thenReturn(Optional.of(existing));
        final TrackingIntegrationService service = new TrackingIntegrationService(
                repository, tokenService, providerRegistry);

        service.save(TrackingProviderId.INPOST, true,
                values("PRODUCTION", " new-client ", ""));

        assertEquals("existing-secret", existing.getValue("clientSecret"));
        assertEquals("new-client", existing.getValue("clientId"));
        assertEquals("PRODUCTION", existing.getValue("environment"));
        verify(repository).update(existing);
        verify(tokenService).invalidate(existing.getConfigurationId());
    }

    @Test
    void shouldRequireSecretForNewConfiguration() {
        final TrackingIntegrationRepository repository = mock(TrackingIntegrationRepository.class);
        final TrackingTokenServicePort tokenService = mock(TrackingTokenServicePort.class);
        when(repository.findByProvider(TrackingProviderId.INPOST)).thenReturn(Optional.empty());
        final TrackingIntegrationService service = new TrackingIntegrationService(
                repository, tokenService, providerRegistry());

        assertThrows(TrackingException.class, () -> service.save(
                TrackingProviderId.INPOST, true, values("STAGE", "client", "")));
    }

    @Test
    void shouldCreateConfigurationAndInvalidateAnyCachedToken() {
        final TrackingIntegrationRepository repository = mock(TrackingIntegrationRepository.class);
        final TrackingTokenServicePort tokenService = mock(TrackingTokenServicePort.class);
        when(repository.findByProvider(TrackingProviderId.INPOST)).thenReturn(Optional.empty());
        final TrackingIntegrationService service = new TrackingIntegrationService(
                repository, tokenService, providerRegistry());
        final ArgumentCaptor<TrackingIntegrationConfiguration> captor =
                ArgumentCaptor.forClass(TrackingIntegrationConfiguration.class);

        service.save(TrackingProviderId.INPOST, false, values("STAGE", "client", "secret"));

        verify(repository).create(captor.capture());
        verify(tokenService).invalidate(captor.getValue().getConfigurationId());
        assertEquals("secret", captor.getValue().getValue("clientSecret"));
    }

    private TrackingProviderRegistry providerRegistry() {
        final TrackingProviderRegistry providerRegistry = mock(TrackingProviderRegistry.class);
        when(providerRegistry.integrationDefinition(TrackingProviderId.INPOST)).thenReturn(
                new TrackingIntegrationDefinition(TrackingProviderId.INPOST, "InPost", java.util.List.of(
                        new TrackingIntegrationFieldDefinition(
                                "environment", "Environment", TrackingIntegrationFieldType.SELECT,
                                true, "STAGE", 32, java.util.List.of(
                                new TrackingIntegrationFieldOption("STAGE", "Stage"),
                                new TrackingIntegrationFieldOption("PRODUCTION", "Production"))),
                        new TrackingIntegrationFieldDefinition(
                                "clientId", "Client ID", TrackingIntegrationFieldType.TEXT,
                                true, "", 512, java.util.List.of()),
                        new TrackingIntegrationFieldDefinition(
                                "clientSecret", "Client secret", TrackingIntegrationFieldType.SECRET,
                                true, "", 1024, java.util.List.of()))));
        return providerRegistry;
    }

    private Map<String, String> values(final String environment,
                                       final String clientId,
                                       final String clientSecret) {
        return Map.of("environment", environment, "clientId", clientId, "clientSecret", clientSecret);
    }
}
