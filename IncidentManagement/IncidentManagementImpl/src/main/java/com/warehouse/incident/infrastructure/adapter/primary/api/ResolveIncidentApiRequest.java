package com.warehouse.incident.infrastructure.adapter.primary.api;

import com.warehouse.incident.domain.enumeration.IncidentResolution;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ResolveIncidentApiRequest(@NotNull IncidentResolution resolution,
                                        @Size(max = 2000) String note) {
}
