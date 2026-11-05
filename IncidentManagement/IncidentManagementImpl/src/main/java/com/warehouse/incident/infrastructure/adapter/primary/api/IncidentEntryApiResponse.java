package com.warehouse.incident.infrastructure.adapter.primary.api;

import com.warehouse.incident.domain.enumeration.IncidentEntryType;

import java.time.Instant;

public record IncidentEntryApiResponse(IncidentEntryType type,
                                       IncidentActorApiResponse actor,
                                       String text,
                                       Instant occurredAt) {
}
