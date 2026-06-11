package com.warehouse.tracking.domain.port.secondary;

import java.util.List;

import com.warehouse.tracking.domain.enumeration.TrackingProviderId;
import com.warehouse.tracking.domain.model.ExternalTrackingResult;
import com.warehouse.tracking.domain.model.TrackingIntegrationConfiguration;
import com.warehouse.tracking.domain.model.TrackingIntegrationDefinition;

public interface TrackingProviderServicePort {

    TrackingProviderId providerId();

    TrackingIntegrationDefinition integrationDefinition();

    boolean isAvailable();

    List<ExternalTrackingResult> track(final List<String> trackingNumbers);

    void testConnection(final TrackingIntegrationConfiguration configuration);
}
