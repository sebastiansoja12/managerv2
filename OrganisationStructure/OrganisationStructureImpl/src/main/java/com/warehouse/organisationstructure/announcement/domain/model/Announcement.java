package com.warehouse.organisationstructure.announcement.domain.model;

import com.warehouse.commonassets.identificator.AnnouncementId;

import java.time.Instant;
import java.util.Objects;

public final class Announcement {

    private final AnnouncementId announcementId;
    private final String message;
    private final Instant createdAt;
    private final Instant updatedAt;

    private Announcement(final AnnouncementId announcementId,
                         final String message,
                         final Instant createdAt,
                         final Instant updatedAt) {
        this.announcementId = announcementId;
        this.message = requireMessage(message);
        this.createdAt = Objects.requireNonNull(createdAt, "Created at must not be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "Updated at must not be null");
    }

    public static Announcement create(final String message, final Instant now) {
        return new Announcement(null, message, now, now);
    }

    public Announcement updateMessage(final String nextMessage, final Instant now) {
        return new Announcement(announcementId, nextMessage, createdAt, now);
    }

    public static Announcement restore(final AnnouncementId announcementId,
                                       final String message,
                                       final Instant createdAt,
                                       final Instant updatedAt) {
        return new Announcement(announcementId, message, createdAt, updatedAt);
    }

    private static String requireMessage(final String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Announcement message must not be blank");
        }
        return value.trim();
    }

    public AnnouncementId getAnnouncementId() {
        return announcementId;
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
