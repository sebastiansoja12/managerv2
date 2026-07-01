package com.warehouse.process.infrastructure.event;

import java.time.LocalDateTime;

import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.commonassets.identificator.ProcessId;

public class ProcessResponseChangedEvent extends ProcessChangedEvent implements ProcessLogEvent {

    private final String response;

    public ProcessResponseChangedEvent(final ProcessId processLogId,
                                       final ServiceType serviceType,
                                       final LocalDateTime createdAt,
                                       final String response) {
        super(processLogId, serviceType, createdAt);
        this.response = response;
    }

    public String getResponse() {
        return response;
    }
}
