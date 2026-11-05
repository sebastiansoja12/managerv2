package com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.primary.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record DeliveryAreaApiRequest(Long deliveryAreaId,
                                     @NotBlank String name,
                                     @NotNull List<@Valid @NotNull GeoPointApi> points) {
}
