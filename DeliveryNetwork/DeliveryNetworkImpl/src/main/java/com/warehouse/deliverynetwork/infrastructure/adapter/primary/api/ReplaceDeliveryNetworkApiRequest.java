package com.warehouse.deliverynetwork.infrastructure.adapter.primary.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ReplaceDeliveryNetworkApiRequest(
        @Valid @NotNull List<@Valid @NotNull DepartmentConnectionApi> connections) {
}
