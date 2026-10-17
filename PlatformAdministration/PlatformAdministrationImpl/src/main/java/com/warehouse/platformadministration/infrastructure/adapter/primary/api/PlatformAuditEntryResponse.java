package com.warehouse.platformadministration.infrastructure.adapter.primary.api;

import java.time.Instant;

public record PlatformAuditEntryResponse(Long id,
                                         Long platformOwnerId,
                                         String platformOwnerUsername,
                                         Long operatorId,
                                         Long impersonatedUserId,
                                         boolean impersonation,
                                         String httpMethod,
                                         String path,
                                         int statusCode,
                                         boolean success,
                                         Instant createdAt) {
}
