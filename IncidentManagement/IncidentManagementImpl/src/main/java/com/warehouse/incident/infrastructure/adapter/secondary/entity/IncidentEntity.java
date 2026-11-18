package com.warehouse.incident.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.IncidentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.Username;
import com.warehouse.commonassets.model.BelongsToOperator;
import com.warehouse.incident.domain.enumeration.IncidentCategory;
import com.warehouse.incident.domain.enumeration.IncidentResolution;
import com.warehouse.incident.domain.enumeration.IncidentSeverity;
import com.warehouse.incident.domain.enumeration.IncidentStatus;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity(name = "incident.IncidentEntity")
@Table(name = "incident")
public class IncidentEntity extends BelongsToOperator {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private IncidentId incidentId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "shipment_id", nullable = false))
    private ShipmentId shipmentId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "tracking_number", length = 100))
    private TrackingNumber trackingNumber;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "department_code", length = 40))
    private DepartmentCode departmentCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 40)
    private IncidentCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false, length = 20)
    private IncidentSeverity severity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private IncidentStatus status;

    @Column(name = "description", length = 2000)
    private String description;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "reporter_user_id", nullable = false))
    private UserId reporterUserId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "reporter_username"))
    private Username reporterUsername;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "assignee_user_id"))
    private UserId assigneeUserId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "assignee_username"))
    private Username assigneeUsername;

    @Enumerated(EnumType.STRING)
    @Column(name = "resolution", length = 30)
    private IncidentResolution resolution;

    @Column(name = "reported_at", nullable = false)
    private Instant reportedAt;

    @Column(name = "resolved_at")
    private Instant resolvedAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "incident_entry", joinColumns = @JoinColumn(name = "incident_id", nullable = false))
    @OrderColumn(name = "position")
    private List<IncidentEntryEmbeddable> entries = new ArrayList<>();

    public IncidentEntity(final IncidentId incidentId,
                          final ShipmentId shipmentId,
                          final TrackingNumber trackingNumber,
                          final DepartmentCode departmentCode,
                          final IncidentCategory category,
                          final IncidentSeverity severity,
                          final IncidentStatus status,
                          final String description,
                          final UserId reporterUserId,
                          final Username reporterUsername,
                          final UserId assigneeUserId,
                          final Username assigneeUsername,
                          final IncidentResolution resolution,
                          final Instant reportedAt,
                          final Instant resolvedAt,
                          final Instant updatedAt,
                          final List<IncidentEntryEmbeddable> entries) {
        this.incidentId = incidentId;
        this.shipmentId = shipmentId;
        this.trackingNumber = trackingNumber;
        this.departmentCode = departmentCode;
        this.category = category;
        this.severity = severity;
        this.status = status;
        this.description = description;
        this.reporterUserId = reporterUserId;
        this.reporterUsername = reporterUsername;
        this.assigneeUserId = assigneeUserId;
        this.assigneeUsername = assigneeUsername;
        this.resolution = resolution;
        this.reportedAt = reportedAt;
        this.resolvedAt = resolvedAt;
        this.updatedAt = updatedAt;
        this.entries = new ArrayList<>(entries);
    }
}
