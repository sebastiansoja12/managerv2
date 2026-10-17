package com.warehouse.organisationstructure.announcement.domain.model;

import com.warehouse.commonassets.identificator.AnnouncementId;
import com.warehouse.organisationstructure.announcement.domain.vo.AnnouncementAudience;
import com.warehouse.organisationstructure.announcement.domain.vo.AnnouncementDraft;

import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public final class Announcement {

    public static final int MAX_TITLE_LENGTH = 200;

    public static final int MAX_MESSAGE_LENGTH = 2000;

    public static final Set<String> SUPPORTED_ROLES = Set.of("USER", "SUPPLIER", "MANAGER", "ADMIN", "SUPERADMIN");

    private final AnnouncementId announcementId;

    private final String title;

    private final String message;

    private final AnnouncementSeverity severity;

    private final Instant validFrom;

    private final Instant validTo;

    private final Set<Long> targetOperatorIds;

    private final Set<String> targetRoles;

    private final String createdBy;

    private final Instant createdAt;

    private final Instant updatedAt;

    private Announcement(final AnnouncementId announcementId,
                         final String title,
                         final String message,
                         final AnnouncementSeverity severity,
                         final Instant validFrom,
                         final Instant validTo,
                         final Set<Long> targetOperatorIds,
                         final Set<String> targetRoles,
                         final String createdBy,
                         final Instant createdAt,
                         final Instant updatedAt) {
        this.announcementId = announcementId;
        this.title = normalizeTitle(title);
        this.message = requireMessage(message);
        this.severity = severity != null ? severity : AnnouncementSeverity.INFO;
        if (validFrom != null && validTo != null && !validTo.isAfter(validFrom)) {
            throw new IllegalArgumentException("Announcement validTo must be after validFrom");
        }
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.targetOperatorIds = copyOperatorIds(targetOperatorIds);
        this.targetRoles = copyRoles(targetRoles);
        this.createdBy = createdBy;
        this.createdAt = Objects.requireNonNull(createdAt, "Created at must not be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "Updated at must not be null");
    }

    public static Announcement create(final AnnouncementDraft draft, final String createdBy, final Instant now) {
        return new Announcement(null, draft.title(), draft.message(), draft.severity(), draft.validFrom(),
                draft.validTo(), draft.targetOperatorIds(), draft.targetRoles(), createdBy, now, now);
    }

    public Announcement update(final AnnouncementDraft draft, final Instant now) {
        return new Announcement(announcementId, draft.title(), draft.message(), draft.severity(), draft.validFrom(),
                draft.validTo(), draft.targetOperatorIds(), draft.targetRoles(), createdBy, createdAt, now);
    }

    public static Announcement restore(final AnnouncementId announcementId,
                                       final String title,
                                       final String message,
                                       final AnnouncementSeverity severity,
                                       final Instant validFrom,
                                       final Instant validTo,
                                       final Set<Long> targetOperatorIds,
                                       final Set<String> targetRoles,
                                       final String createdBy,
                                       final Instant createdAt,
                                       final Instant updatedAt) {
        return new Announcement(announcementId, title, message, severity, validFrom, validTo, targetOperatorIds,
                targetRoles, createdBy, createdAt, updatedAt);
    }

    public AnnouncementStatus statusAt(final Instant now) {
        if (validFrom != null && now.isBefore(validFrom)) {
            return AnnouncementStatus.SCHEDULED;
        }
        if (validTo != null && !now.isBefore(validTo)) {
            return AnnouncementStatus.EXPIRED;
        }
        return AnnouncementStatus.ACTIVE;
    }

    public boolean isVisibleTo(final AnnouncementAudience audience, final Instant now) {
        if (statusAt(now) != AnnouncementStatus.ACTIVE) {
            return false;
        }
        if (audience == null) {
            return true;
        }
        final boolean operatorMatches = targetOperatorIds.isEmpty()
                || (audience.operatorId() != null && targetOperatorIds.contains(audience.operatorId()));
        final boolean roleMatches = targetRoles.isEmpty()
                || (audience.role() != null && targetRoles.contains(audience.role()));
        return operatorMatches && roleMatches;
    }

    public boolean isGlobal() {
        return targetOperatorIds.isEmpty() && targetRoles.isEmpty();
    }

    private static String normalizeTitle(final String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        final String normalized = value.trim();
        if (normalized.length() > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException("Announcement title cannot be longer than " + MAX_TITLE_LENGTH);
        }
        return normalized;
    }

    private static String requireMessage(final String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Announcement message must not be blank");
        }
        final String normalized = value.trim();
        if (normalized.length() > MAX_MESSAGE_LENGTH) {
            throw new IllegalArgumentException("Announcement message cannot be longer than " + MAX_MESSAGE_LENGTH);
        }
        return normalized;
    }

    private static Set<Long> copyOperatorIds(final Set<Long> operatorIds) {
        if (operatorIds == null || operatorIds.isEmpty()) {
            return Set.of();
        }
        if (operatorIds.stream().anyMatch(operatorId -> operatorId == null || operatorId <= 0)) {
            throw new IllegalArgumentException("Announcement target operators must be positive identifiers");
        }
        return Collections.unmodifiableSet(new LinkedHashSet<>(operatorIds));
    }

    private static Set<String> copyRoles(final Set<String> roles) {
        if (roles == null || roles.isEmpty()) {
            return Set.of();
        }
        if (!SUPPORTED_ROLES.containsAll(roles)) {
            throw new IllegalArgumentException("Announcement target roles must be one of " + SUPPORTED_ROLES);
        }
        return Collections.unmodifiableSet(new LinkedHashSet<>(roles));
    }

    public AnnouncementId getAnnouncementId() {
        return announcementId;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public AnnouncementSeverity getSeverity() {
        return severity;
    }

    public Instant getValidFrom() {
        return validFrom;
    }

    public Instant getValidTo() {
        return validTo;
    }

    public Set<Long> getTargetOperatorIds() {
        return targetOperatorIds;
    }

    public Set<String> getTargetRoles() {
        return targetRoles;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
