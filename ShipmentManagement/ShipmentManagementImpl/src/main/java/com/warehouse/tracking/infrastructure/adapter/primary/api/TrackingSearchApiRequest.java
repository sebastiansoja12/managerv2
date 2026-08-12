package com.warehouse.tracking.infrastructure.adapter.primary.api;

import java.util.List;

import com.warehouse.tracking.domain.enumeration.TrackingProviderId;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TrackingSearchApiRequest(@NotNull TrackingProviderId provider,
                                       @NotEmpty @Size(max = 100)
                                       List<@NotBlank @Size(max = 64) String> trackingNumbers) {
}
