package com.warehouse.organisationstructure.announcement.infrastructure.adapter.primary.mapper;

import com.warehouse.organisationstructure.announcement.domain.model.Announcement;
import com.warehouse.organisationstructure.api.dto.AnnouncementDto;

public final class AnnouncementResponseMapper {

    private AnnouncementResponseMapper() {
    }

    public static AnnouncementDto toDto(final Announcement announcement) {
        return new AnnouncementDto(
                announcement.getAnnouncementId(),
                announcement.getMessage(),
                announcement.getCreatedAt(),
                announcement.getUpdatedAt()
        );
    }
}
