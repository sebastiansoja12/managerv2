package com.warehouse.organisationstructure.announcement.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.AnnouncementId;
import com.warehouse.organisationstructure.announcement.domain.model.Announcement;
import com.warehouse.organisationstructure.announcement.domain.model.AnnouncementSeverity;
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
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = "organisationstructure.AnnouncementEntity")
@Table(name = "announcements")
public class AnnouncementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", length = 200)
    private String title;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false, length = 20)
    private AnnouncementSeverity severity;

    @Column(name = "valid_from")
    private Instant validFrom;

    @Column(name = "valid_to")
    private Instant validTo;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "announcement_target_operators", joinColumns = @JoinColumn(name = "announcement_id"))
    @Column(name = "operator_id", nullable = false)
    private Set<Long> targetOperatorIds = new LinkedHashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "announcement_target_roles", joinColumns = @JoinColumn(name = "announcement_id"))
    @Column(name = "role", nullable = false, length = 30)
    private Set<String> targetRoles = new LinkedHashSet<>();

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected AnnouncementEntity() {
    }

    public static AnnouncementEntity from(final Announcement announcement) {
        final AnnouncementEntity entity = new AnnouncementEntity();
        entity.id = announcement.getAnnouncementId() != null ? announcement.getAnnouncementId().getValue() : null;
        entity.title = announcement.getTitle();
        entity.message = announcement.getMessage();
        entity.severity = announcement.getSeverity();
        entity.validFrom = announcement.getValidFrom();
        entity.validTo = announcement.getValidTo();
        entity.targetOperatorIds = new LinkedHashSet<>(announcement.getTargetOperatorIds());
        entity.targetRoles = new LinkedHashSet<>(announcement.getTargetRoles());
        entity.createdBy = announcement.getCreatedBy();
        entity.createdAt = announcement.getCreatedAt();
        entity.updatedAt = announcement.getUpdatedAt();
        return entity;
    }

    public Announcement toModel() {
        return Announcement.restore(AnnouncementId.of(id), title, message, severity, validFrom, validTo,
                targetOperatorIds, targetRoles, createdBy, createdAt, updatedAt);
    }

    public Long getId() {
        return id;
    }
}
