package com.warehouse.process.infrastructure.event;

import java.time.LocalDateTime;

import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.process.infrastructure.dto.ProcessStatusDto;

public class ProcessFinishEvent extends ProcessChangedEvent implements ProcessLogEvent {

    private final ProcessStatusDto processStatus;
    private final String faultDescription;

    public ProcessFinishEvent(final ProcessId processId, final ServiceType serviceType,
                              final ProcessStatusDto processStatus, final LocalDateTime createdAt) {
        this(processId, serviceType, processStatus, createdAt, null);
    }

    public ProcessFinishEvent(final ProcessId processId, final ServiceType serviceType,
                              final ProcessStatusDto processStatus, final LocalDateTime createdAt,
                              final String faultDescription) {
        super(processId, serviceType, createdAt);
        this.processStatus = processStatus;
        this.faultDescription = faultDescription;
    }

    public ProcessStatusDto getProcessStatus() {
        return processStatus;
    }

    public String getFaultDescription() {
        return faultDescription;
    }
}
