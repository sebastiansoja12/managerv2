package com.warehouse.process.infrastructure.event;

import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.process.infrastructure.dto.ProcessLogId;

public class ProcessChangedEvent implements ProcessLogEvent {
    private final ProcessLogId processLogId;
    private final ServiceType serviceType;

    public ProcessChangedEvent(final ProcessLogId processLogId,
                               final ServiceType serviceType) {
        this.processLogId = processLogId;
        this.serviceType = serviceType;
    }

    public ProcessLogId getProcessLogId() {
        return processLogId;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }
}
