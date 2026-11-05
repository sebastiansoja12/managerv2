package com.warehouse.incident.infrastructure.adapter.primary.api;

import com.warehouse.incident.domain.enumeration.IncidentCategory;
import com.warehouse.incident.domain.enumeration.IncidentSeverity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReportIncidentApiRequest(@NotNull ShipmentIdDto shipmentId,
                                       @NotNull IncidentCategory category,
                                       @NotNull IncidentSeverity severity,
                                       @Size(max = 2000) String description) {
}
