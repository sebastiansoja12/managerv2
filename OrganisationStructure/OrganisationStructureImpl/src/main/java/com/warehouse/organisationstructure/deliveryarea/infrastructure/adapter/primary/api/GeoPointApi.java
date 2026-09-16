package com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.primary.api;

import jakarta.validation.constraints.NotNull;

public record GeoPointApi(@NotNull Double latitude, @NotNull Double longitude) {
}
