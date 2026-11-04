package com.warehouse.incident.infrastructure.adapter.primary.api;

import com.warehouse.incident.domain.enumeration.IncidentCategory;
import com.warehouse.incident.domain.enumeration.IncidentResolution;
import com.warehouse.incident.domain.enumeration.IncidentSeverity;
import com.warehouse.incident.domain.enumeration.IncidentStatus;

import java.time.Instant;
import java.util.List;

public record IncidentApiResponse(IncidentIdDto incidentId,
                                  ShipmentIdDto shipmentId,
                                  String trackingNumber,
                                  String departmentCode,
                                  IncidentCategory category,
                                  IncidentSeverity severity,
                                  IncidentStatus status,
                                  String description,
                                  IncidentActorApiResponse reporter,
                                  IncidentActorApiResponse assignee,
                                  IncidentResolution resolution,
                                  Instant reportedAt,
                                  Instant resolvedAt,
                                  Instant updatedAt,
                                  List<IncidentEntryApiResponse> entries) {
}
