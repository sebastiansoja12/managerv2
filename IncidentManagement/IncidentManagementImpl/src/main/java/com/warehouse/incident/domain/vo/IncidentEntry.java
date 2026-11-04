package com.warehouse.incident.domain.vo;

import com.warehouse.incident.domain.enumeration.IncidentEntryType;

import java.time.Instant;
import java.util.Objects;

public record IncidentEntry(IncidentEntryType type,
                            IncidentActor actor,
                            String text,
                            Instant occurredAt) {

    public IncidentEntry {
        Objects.requireNonNull(type, "Incident entry type cannot be null");
        Objects.requireNonNull(actor, "Incident entry actor cannot be null");
        Objects.requireNonNull(occurredAt, "Incident entry occurrence time cannot be null");
    }
}
