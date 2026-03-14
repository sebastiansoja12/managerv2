package com.warehouse.process.infrastructure.event;

import java.time.LocalDateTime;

import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.commonassets.identificator.ProcessId;

public class ProcessChangedEvent implements ProcessLogEvent {
    private final ProcessId processLogId;
    private final ServiceType serviceType;
    private final LocalDateTime createdAt;

    public ProcessChangedEvent(final ProcessId processLogId,
                               final ServiceType serviceType,
                               final LocalDateTime createdAt) {
        this.processLogId = processLogId;
        this.serviceType = serviceType;
        this.createdAt = createdAt;
    }

    public ProcessId getProcessLogId() {
        return processLogId;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
