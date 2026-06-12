package com.warehouse.organisationstructure.announcement.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.AnnouncementId;
import com.warehouse.organisationstructure.announcement.domain.model.Announcement;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity(name = "organisationstructure.AnnouncementEntity")
@Table(name = "announcements")
public class AnnouncementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected AnnouncementEntity() {
    }

    private AnnouncementEntity(final Long id,
                               final String message,
                               final Instant createdAt,
                               final Instant updatedAt) {
        this.id = id;
        this.message = message;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static AnnouncementEntity from(final Announcement announcement) {
        return new AnnouncementEntity(
                announcement.getAnnouncementId() != null ? announcement.getAnnouncementId().getValue() : null,
                announcement.getMessage(),
                announcement.getCreatedAt(),
                announcement.getUpdatedAt()
        );
    }

    public Announcement toModel() {
        return Announcement.restore(AnnouncementId.of(id), message, createdAt, updatedAt);
    }

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
