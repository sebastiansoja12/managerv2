package com.warehouse.process.infrastructure.event;

import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.process.infrastructure.dto.ProcessLogId;

public class ProcessInitializeEvent extends ProcessChangedEvent implements ProcessLogEvent {
    public ProcessInitializeEvent(final ProcessLogId processLogId, final ServiceType serviceType) {
        super(processLogId, serviceType);
    }
}
