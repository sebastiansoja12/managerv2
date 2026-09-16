package com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.primary.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ReplaceDeliveryAreasApiRequest(@NotNull List<@Valid @NotNull DeliveryAreaApiRequest> areas) {
}
