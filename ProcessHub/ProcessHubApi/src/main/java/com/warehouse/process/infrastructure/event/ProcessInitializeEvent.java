package com.warehouse.process.infrastructure.event;

import java.time.LocalDateTime;

import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.process.infrastructure.dto.ProcessLogId;

public class ProcessInitializeEvent extends ProcessChangedEvent implements ProcessLogEvent {
    public ProcessInitializeEvent(final ProcessLogId processLogId, final ServiceType serviceType, final LocalDateTime createdAt) {
        super(processLogId, serviceType, createdAt);
    }
}
