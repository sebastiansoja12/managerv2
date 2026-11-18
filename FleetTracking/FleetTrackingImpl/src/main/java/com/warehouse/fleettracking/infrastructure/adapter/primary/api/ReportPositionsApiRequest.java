package com.warehouse.fleettracking.infrastructure.adapter.primary.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ReportPositionsApiRequest(@NotEmpty @Size(max = 500) List<@Valid PositionFixApiRequest> positions) {
}
