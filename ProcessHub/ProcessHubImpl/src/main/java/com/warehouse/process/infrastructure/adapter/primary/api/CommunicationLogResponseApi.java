package com.warehouse.process.infrastructure.adapter.primary.api;

import java.time.Instant;

public record CommunicationLogResponseApi(
        Long id,
        String deviceId,
        String processType,
        String serviceType,
        Long createdBy,
        Long updatedBy,
        String departmentCode,
        String sourceService,
        String targetService,
        String request,
        String response,
        Instant createdAt,
        Instant updatedAt,
        String faultDescription) {
}
