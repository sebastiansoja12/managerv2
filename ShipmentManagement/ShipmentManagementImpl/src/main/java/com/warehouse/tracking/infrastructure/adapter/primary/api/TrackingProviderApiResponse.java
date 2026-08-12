package com.warehouse.tracking.infrastructure.adapter.primary.api;

import com.warehouse.tracking.domain.enumeration.TrackingProviderId;

public record TrackingProviderApiResponse(TrackingProviderId id, String displayName) {

    public static TrackingProviderApiResponse from(final TrackingProviderId providerId) {
        return new TrackingProviderApiResponse(providerId, providerId.getDisplayName());
    }
}
