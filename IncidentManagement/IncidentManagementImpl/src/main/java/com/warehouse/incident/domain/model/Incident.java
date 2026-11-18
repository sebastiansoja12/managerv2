package com.warehouse.incident.domain.model;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.IncidentId;
import com.warehouse.incident.domain.enumeration.IncidentCategory;
import com.warehouse.incident.domain.enumeration.IncidentEntryType;
import com.warehouse.incident.domain.enumeration.IncidentResolution;
import com.warehouse.incident.domain.enumeration.IncidentSeverity;
import com.warehouse.incident.domain.enumeration.IncidentStatus;
import com.warehouse.incident.domain.exception.IncidentException;
import com.warehouse.incident.domain.vo.IncidentActor;
import com.warehouse.incident.domain.vo.IncidentEntry;
import com.warehouse.incident.domain.vo.IncidentShipment;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Incident {

    private final IncidentId incidentId;

    private final IncidentShipment shipment;

    private final DepartmentCode departmentCode;

    private final IncidentCategory category;

    private final IncidentSeverity severity;

    private final String description;

    private final IncidentActor reporter;

    private final Instant reportedAt;

    private final List<IncidentEntry> entries;

    private IncidentStatus status;

    private IncidentActor assignee;

    private IncidentResolution resolution;

    private Instant resolvedAt;

    private Instant updatedAt;

    private Incident(final IncidentId incidentId,
                     final IncidentShipment shipment,
                     final DepartmentCode departmentCode,
                     final IncidentCategory category,
                     final IncidentSeverity severity,
                     final String description,
                     final IncidentActor reporter,
                     final Instant reportedAt,
                     final List<IncidentEntry> entries,
                     final IncidentStatus status,
                     final IncidentActor assignee,
                     final IncidentResolution resolution,
                     final Instant resolvedAt,
                     final Instant updatedAt) {
        this.incidentId = Objects.requireNonNull(incidentId, "Incident ID cannot be null");
        this.shipment = Objects.requireNonNull(shipment, "Incident shipment cannot be null");
        this.departmentCode = departmentCode;
        this.category = Objects.requireNonNull(category, "Incident category cannot be null");
        this.severity = Objects.requireNonNull(severity, "Incident severity cannot be null");
        this.description = description;
        this.reporter = Objects.requireNonNull(reporter, "Incident reporter cannot be null");
        this.reportedAt = Objects.requireNonNull(reportedAt, "Incident report time cannot be null");
        this.entries = new ArrayList<>(entries);
        this.status = Objects.requireNonNull(status, "Incident status cannot be null");
        this.assignee = assignee;
        this.resolution = resolution;
        this.resolvedAt = resolvedAt;
        this.updatedAt = updatedAt;
    }

    public static Incident report(final IncidentShipment shipment,
                                  final DepartmentCode departmentCode,
                                  final IncidentCategory category,
                                  final IncidentSeverity severity,
                                  final String description,
                                  final IncidentActor reporter,
                                  final Instant reportedAt) {
        final String normalizedDescription = normalize(description);
        if (IncidentCategory.OTHER.equals(category) && normalizedDescription == null) {
            throw new IncidentException(IncidentException.Reason.INVALID_REQUEST,
                    "Incident description is required for category OTHER");
        }
        return new Incident(IncidentId.nextId(), shipment, departmentCode, category, severity, normalizedDescription, reporter,
                reportedAt, List.of(new IncidentEntry(IncidentEntryType.REPORTED, reporter, normalizedDescription,
                reportedAt)), IncidentStatus.OPEN, null, null, null, reportedAt);
    }

    public static Incident rehydrate(final IncidentId incidentId,
                                     final IncidentShipment shipment,
                                     final DepartmentCode departmentCode,
                                     final IncidentCategory category,
                                     final IncidentSeverity severity,
                                     final String description,
                                     final IncidentActor reporter,
                                     final Instant reportedAt,
                                     final List<IncidentEntry> entries,
                                     final IncidentStatus status,
                                     final IncidentActor assignee,
                                     final IncidentResolution resolution,
                                     final Instant resolvedAt,
                                     final Instant updatedAt) {
        return new Incident(incidentId, shipment, departmentCode, category, severity, description, reporter,
                reportedAt, entries, status, assignee, resolution, resolvedAt, updatedAt);
    }

    public void assign(final IncidentActor actor, final Instant assignedAt) {
        requireNotResolved();
        if (this.assignee != null && this.assignee.userId().equals(actor.userId())) {
            return;
        }
        this.assignee = actor;
        this.status = IncidentStatus.IN_PROGRESS;
        record(new IncidentEntry(IncidentEntryType.ASSIGNED, actor, null, assignedAt));
    }

    public void comment(final IncidentActor actor, final String text, final Instant commentedAt) {
        final String normalizedText = normalize(text);
        if (normalizedText == null) {
            throw new IncidentException(IncidentException.Reason.INVALID_REQUEST, "Incident comment cannot be blank");
        }
        record(new IncidentEntry(IncidentEntryType.COMMENTED, actor, normalizedText, commentedAt));
    }

    public void resolve(final IncidentActor actor,
                        final IncidentResolution resolution,
                        final String note,
                        final Instant resolvedAt) {
        requireNotResolved();
        this.status = IncidentStatus.RESOLVED;
        this.resolution = Objects.requireNonNull(resolution, "Incident resolution cannot be null");
        this.resolvedAt = resolvedAt;
        record(new IncidentEntry(IncidentEntryType.RESOLVED, actor, normalize(note), resolvedAt));
    }

    public void reopen(final IncidentActor actor, final String reason, final Instant reopenedAt) {
        if (!IncidentStatus.RESOLVED.equals(this.status)) {
            throw new IncidentException(IncidentException.Reason.CONFLICT, "Only a resolved incident can be reopened");
        }
        final String normalizedReason = normalize(reason);
        if (normalizedReason == null) {
            throw new IncidentException(IncidentException.Reason.INVALID_REQUEST,
                    "Incident reopen reason cannot be blank");
        }
        this.status = this.assignee == null ? IncidentStatus.OPEN : IncidentStatus.IN_PROGRESS;
        this.resolution = null;
        this.resolvedAt = null;
        record(new IncidentEntry(IncidentEntryType.REOPENED, actor, normalizedReason, reopenedAt));
    }

    private void requireNotResolved() {
        if (IncidentStatus.RESOLVED.equals(this.status)) {
            throw new IncidentException(IncidentException.Reason.CONFLICT, "Incident is already resolved");
        }
    }

    private void record(final IncidentEntry entry) {
        this.entries.add(entry);
        this.updatedAt = entry.occurredAt();
    }

    private static String normalize(final String value) {
        return value == null || value.isBlank() ? null : value.strip();
    }

    public IncidentId incidentId() {
        return incidentId;
    }

    public IncidentShipment shipment() {
        return shipment;
    }

    public DepartmentCode departmentCode() {
        return departmentCode;
    }

    public IncidentCategory category() {
        return category;
    }

    public IncidentSeverity severity() {
        return severity;
    }

    public String description() {
        return description;
    }

    public IncidentActor reporter() {
        return reporter;
    }

    public Instant reportedAt() {
        return reportedAt;
    }

    public List<IncidentEntry> entries() {
        return List.copyOf(entries);
    }

    public IncidentStatus status() {
        return status;
    }

    public IncidentActor assignee() {
        return assignee;
    }

    public IncidentResolution resolution() {
        return resolution;
    }

    public Instant resolvedAt() {
        return resolvedAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
