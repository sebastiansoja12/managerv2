package com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.primary.api;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record GenerateDeliveryAreasApiRequest(@NotEmpty List<@NotNull Long> departmentIds) {
}
