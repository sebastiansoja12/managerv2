package com.warehouse.tracking.domain.port.secondary;

import java.util.UUID;

import com.warehouse.tracking.domain.model.TrackingIntegrationConfiguration;

public interface TrackingTokenServicePort {

    String accessToken(final TrackingIntegrationConfiguration configuration, final String requestId);

    void invalidate(final UUID configurationId);
}
