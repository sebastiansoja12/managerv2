package com.warehouse.auditlog.infrastructure.adapter.secondary.entity;

import com.warehouse.auditlog.domain.enumeration.AuditLogResourceType;
import com.warehouse.commonassets.model.BelongsToOperator;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity(name = "auditLog.AuditLogEntryEntity")
@Table(name = "audit_log_entry")
public class AuditLogEntryEntity extends BelongsToOperator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", unique = true)
    private UUID eventId;

    @Enumerated(EnumType.STRING)
    @Column(name = "resource_type", nullable = false, length = 40)
    private AuditLogResourceType resourceType;

    @Column(name = "resource_id", nullable = false, length = 100)
    private String resourceId;

    @Column(name = "action", nullable = false, length = 80)
    private String action;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "audit_log_change",
            joinColumns = @JoinColumn(name = "audit_log_entry_id", nullable = false))
    @OrderColumn(name = "position")
    private List<AuditLogChangeEmbeddable> changes = new ArrayList<>();

    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username")
    private String username;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    public AuditLogEntryEntity(final UUID eventId,
                               final AuditLogResourceType resourceType,
                               final String resourceId,
                               final String action,
                               final List<AuditLogChangeEmbeddable> changes,
                               final Long departmentId,
                               final Long userId,
                               final String username,
                               final Instant occurredAt) {
        this.eventId = eventId;
        this.resourceType = resourceType;
        this.resourceId = resourceId;
        this.action = action;
        this.changes = new ArrayList<>(changes);
        this.departmentId = departmentId;
        this.userId = userId;
        this.username = username;
        this.occurredAt = occurredAt;
    }
}
