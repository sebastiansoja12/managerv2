package com.warehouse.tracking.domain.port.secondary;

import java.util.Optional;

import com.warehouse.tracking.domain.enumeration.TrackingProviderId;
import com.warehouse.tracking.domain.model.TrackingIntegrationConfiguration;

public interface TrackingIntegrationRepository {

    Optional<TrackingIntegrationConfiguration> findByProvider(final TrackingProviderId providerId);

    void create(final TrackingIntegrationConfiguration configuration);

    void update(final TrackingIntegrationConfiguration configuration);
}
