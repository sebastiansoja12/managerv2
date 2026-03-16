package com.warehouse.process.domain.vo;

import java.time.Instant;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.process.domain.enumeration.ProcessStatus;
import com.warehouse.process.domain.model.CommunicationLogDetails;
import com.warehouse.process.domain.model.DeviceInformation;

public record ProcessLogSnapshot(
        ProcessId processId,
        String request,
        String response,
        Instant createdAt,
        Instant modifiedAt,
        CommunicationLogDetails communicationLogDetails,
        ProcessStatus status,
        String faultDescription,
        DeviceInformation deviceInformation) {
}
