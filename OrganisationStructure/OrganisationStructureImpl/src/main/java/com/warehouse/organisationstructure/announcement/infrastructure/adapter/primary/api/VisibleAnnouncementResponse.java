package com.warehouse.organisationstructure.announcement.infrastructure.adapter.primary.api;

import java.time.Instant;

public record VisibleAnnouncementResponse(Long id,
                                          String title,
                                          String message,
                                          String severity,
                                          Instant validFrom,
                                          Instant validTo,
                                          Instant updatedAt) {
}
