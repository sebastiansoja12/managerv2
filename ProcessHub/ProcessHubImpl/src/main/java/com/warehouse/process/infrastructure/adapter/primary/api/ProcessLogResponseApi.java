package com.warehouse.process.infrastructure.adapter.primary.api;

import java.time.Instant;
import java.util.List;

public record ProcessLogResponseApi(
        String processId,
        String request,
        String response,
        Instant createdAt,
        Instant modifiedAt,
        String status,
        String faultDescription,
        DeviceInformationResponseApi deviceInformation,
        List<CommunicationLogResponseApi> communicationLogs) {
}
