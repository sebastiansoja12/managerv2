package com.warehouse.organisationstructure.announcement.infrastructure.adapter.primary.mapper;

import com.warehouse.organisationstructure.api.dto.AnnouncementRequestDto;

public final class AnnouncementRequestMapper {

    private AnnouncementRequestMapper() {
    }

    public static String toMessage(final AnnouncementRequestDto request) {
        return request.message().trim();
    }
}
