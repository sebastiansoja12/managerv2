package com.warehouse.organisationstructure.api.dto;

import com.warehouse.commonassets.identificator.AnnouncementId;

import java.time.Instant;

public record AnnouncementDto(
        AnnouncementId id,
        String message,
        Instant createdAt,
        Instant updatedAt
) {
}
