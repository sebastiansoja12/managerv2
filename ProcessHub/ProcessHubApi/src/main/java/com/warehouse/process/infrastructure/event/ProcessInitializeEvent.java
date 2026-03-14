package com.warehouse.process.infrastructure.event;

import java.time.LocalDateTime;

import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.commonassets.identificator.ProcessId;

public class ProcessInitializeEvent extends ProcessChangedEvent implements ProcessLogEvent {
    public ProcessInitializeEvent(final ProcessId processId, final ServiceType serviceType, final LocalDateTime createdAt) {
        super(processId, serviceType, createdAt);
    }
}
