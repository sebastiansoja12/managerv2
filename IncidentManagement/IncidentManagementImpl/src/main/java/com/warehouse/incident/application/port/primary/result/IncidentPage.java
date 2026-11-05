package com.warehouse.incident.application.port.primary.result;

import com.warehouse.incident.domain.model.Incident;

import java.util.List;
import java.util.Objects;

public record IncidentPage(List<Incident> incidents,
                           int page,
                           int size,
                           long totalElements) {

    public IncidentPage {
        incidents = List.copyOf(Objects.requireNonNull(incidents, "Incidents cannot be null"));
    }
}
