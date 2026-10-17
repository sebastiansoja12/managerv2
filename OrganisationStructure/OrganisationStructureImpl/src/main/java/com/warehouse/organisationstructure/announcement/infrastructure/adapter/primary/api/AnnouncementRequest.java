package com.warehouse.organisationstructure.announcement.infrastructure.adapter.primary.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.List;

public record AnnouncementRequest(@Size(max = 200) String title,
                                  @NotBlank @Size(max = 2000) String message,
                                  String severity,
                                  Instant validFrom,
                                  Instant validTo,
                                  List<Long> targetOperatorIds,
                                  List<String> targetRoles) {
}
