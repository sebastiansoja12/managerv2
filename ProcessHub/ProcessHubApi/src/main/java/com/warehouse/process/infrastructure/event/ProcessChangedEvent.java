package com.warehouse.process.infrastructure.event;

import java.time.LocalDateTime;

import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.process.infrastructure.dto.ProcessLogId;

public class ProcessChangedEvent implements ProcessLogEvent {
    private final ProcessLogId processLogId;
    private final ServiceType serviceType;
    private final LocalDateTime createdAt;

    public ProcessChangedEvent(final ProcessLogId processLogId,
                               final ServiceType serviceType,
                               final LocalDateTime createdAt) {
        this.processLogId = processLogId;
        this.serviceType = serviceType;
        this.createdAt = createdAt;
    }

    public ProcessLogId getProcessLogId() {
        return processLogId;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
