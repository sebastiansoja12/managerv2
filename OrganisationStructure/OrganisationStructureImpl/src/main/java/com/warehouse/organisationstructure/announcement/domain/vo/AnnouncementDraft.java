package com.warehouse.organisationstructure.announcement.domain.vo;

import com.warehouse.organisationstructure.announcement.domain.model.AnnouncementSeverity;

import java.time.Instant;
import java.util.Set;

public record AnnouncementDraft(String title,
                                String message,
                                AnnouncementSeverity severity,
                                Instant validFrom,
                                Instant validTo,
                                Set<Long> targetOperatorIds,
                                Set<String> targetRoles) {
}
