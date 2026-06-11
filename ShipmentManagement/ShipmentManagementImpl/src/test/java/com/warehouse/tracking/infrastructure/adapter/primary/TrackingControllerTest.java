package com.warehouse.tracking.infrastructure.adapter.primary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.warehouse.tracking.domain.enumeration.TrackingProviderId;
import com.warehouse.tracking.domain.model.ExternalTrackingResult;
import com.warehouse.tracking.domain.model.TrackingEvent;
import com.warehouse.tracking.domain.model.TrackingIntegrationDefinition;
import com.warehouse.tracking.domain.service.TrackingIntegrationService;
import com.warehouse.tracking.domain.service.TrackingProviderRegistry;
import com.warehouse.tracking.infrastructure.adapter.primary.api.ExternalTrackingApiResponse;
import com.warehouse.tracking.infrastructure.adapter.primary.api.TrackingIntegrationApiResponse;
import com.warehouse.tracking.infrastructure.adapter.primary.api.TrackingSearchApiRequest;

class TrackingControllerTest {

    @Test
    void shouldDelegateExternalSearchAndMapProviderIndependentResponse() {
        final TrackingProviderRegistry registry = mock(TrackingProviderRegistry.class);
        final TrackingIntegrationService integrationService = mock(TrackingIntegrationService.class);
        final OffsetDateTime timestamp = OffsetDateTime.parse("2026-08-12T10:00:00Z");
        final ExternalTrackingResult result = new ExternalTrackingResult(
                TrackingProviderId.INPOST, "TRACK123456", "Accepted", timestamp,
                List.of(new TrackingEvent(timestamp, "Accepted", null, "ACCEPTED", null)),
                null, null, null, null, null, null);
        when(registry.track(TrackingProviderId.INPOST, List.of("TRACK123456")))
                .thenReturn(List.of(result));
        final TrackingController controller = new TrackingController(registry, integrationService);

        final List<ExternalTrackingApiResponse> response = controller.search(
                new TrackingSearchApiRequest(TrackingProviderId.INPOST, List.of("TRACK123456"))).getBody();

        assertEquals(1, response.size());
        assertEquals("ACCEPTED", response.get(0).events().get(0).eventCode());
        verify(registry).track(TrackingProviderId.INPOST, List.of("TRACK123456"));
    }

    @Test
    void shouldListUnconfiguredIntegrationWithoutASecret() {
        final TrackingProviderRegistry registry = mock(TrackingProviderRegistry.class);
        final TrackingIntegrationService integrationService = mock(TrackingIntegrationService.class);
        when(registry.integrationDefinitions()).thenReturn(List.of(
                new TrackingIntegrationDefinition(TrackingProviderId.INPOST, "InPost", List.of())));
        when(integrationService.find(TrackingProviderId.INPOST)).thenReturn(java.util.Optional.empty());
        final TrackingController controller = new TrackingController(registry, integrationService);

        final List<TrackingIntegrationApiResponse> response = controller.integrations().getBody();

        assertEquals(1, response.size());
        assertFalse(response.get(0).configured());
        assertEquals(java.util.Map.of(), response.get(0).values());
    }
}
