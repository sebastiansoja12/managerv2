package com.warehouse.organisationstructure.announcement.infrastructure.adapter.primary.mapper;

import com.warehouse.organisationstructure.announcement.domain.model.Announcement;
import com.warehouse.organisationstructure.announcement.domain.model.AnnouncementSeverity;
import com.warehouse.organisationstructure.announcement.domain.model.AnnouncementStatus;
import com.warehouse.organisationstructure.announcement.domain.vo.AnnouncementDraft;
import com.warehouse.organisationstructure.announcement.infrastructure.adapter.primary.api.AnnouncementRequest;
import com.warehouse.organisationstructure.announcement.infrastructure.adapter.primary.api.PlatformAnnouncementResponse;
import com.warehouse.organisationstructure.announcement.infrastructure.adapter.primary.api.VisibleAnnouncementResponse;
import com.warehouse.organisationstructure.api.dto.AnnouncementDto;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;

public final class AnnouncementResponseMapper {

    private AnnouncementResponseMapper() {
    }

    public static AnnouncementDto toDto(final Announcement announcement) {
        return new AnnouncementDto(announcement.getAnnouncementId(), announcement.getMessage(),
                announcement.getCreatedAt(), announcement.getUpdatedAt());
    }

    public static VisibleAnnouncementResponse toVisibleResponse(final Announcement announcement) {
        return new VisibleAnnouncementResponse(announcement.getAnnouncementId().getValue(), announcement.getTitle(),
                announcement.getMessage(), announcement.getSeverity().name(), announcement.getValidFrom(),
                announcement.getValidTo(), announcement.getUpdatedAt());
    }

    public static PlatformAnnouncementResponse toPlatformResponse(final Announcement announcement,
                                                                  final AnnouncementStatus status) {
        return new PlatformAnnouncementResponse(announcement.getAnnouncementId() != null
                ? announcement.getAnnouncementId().getValue() : null, announcement.getTitle(),
                announcement.getMessage(), announcement.getSeverity().name(), status != null ? status.name() : null,
                announcement.getValidFrom(), announcement.getValidTo(),
                List.copyOf(announcement.getTargetOperatorIds()), List.copyOf(announcement.getTargetRoles()),
                announcement.getCreatedBy(), announcement.getCreatedAt(), announcement.getUpdatedAt());
    }

    public static AnnouncementDraft toDraft(final AnnouncementRequest request) {
        return new AnnouncementDraft(request.title(), request.message(), severity(request.severity()),
                request.validFrom(), request.validTo(),
                request.targetOperatorIds() != null ? new LinkedHashSet<>(request.targetOperatorIds()) : null,
                request.targetRoles() != null ? new LinkedHashSet<>(request.targetRoles()) : null);
    }

    private static AnnouncementSeverity severity(final String value) {
        if (value == null || value.isBlank()) {
            return AnnouncementSeverity.INFO;
        }
        return AnnouncementSeverity.valueOf(value.trim().toUpperCase(Locale.ROOT));
    }
}
