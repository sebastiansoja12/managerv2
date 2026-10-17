package com.warehouse.organisationstructure.announcement.infrastructure.adapter.primary.api;

import java.time.Instant;
import java.util.List;

public record PlatformAnnouncementResponse(Long id,
                                           String title,
                                           String message,
                                           String severity,
                                           String status,
                                           Instant validFrom,
                                           Instant validTo,
                                           List<Long> targetOperatorIds,
                                           List<String> targetRoles,
                                           String createdBy,
                                           Instant createdAt,
                                           Instant updatedAt) {
}
