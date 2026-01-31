package com.warehouse.process.infrastructure.event;

import java.time.LocalDateTime;

import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.process.infrastructure.dto.ProcessLogId;
import com.warehouse.process.infrastructure.dto.ProcessStatusDto;

public class ProcessFinishEvent extends ProcessChangedEvent implements ProcessLogEvent {

    private final ProcessStatusDto processStatus;

    public ProcessFinishEvent(final ProcessLogId processLogId, final ServiceType serviceType,
                              final ProcessStatusDto processStatus, final LocalDateTime createdAt) {
        super(processLogId, serviceType, createdAt);
        this.processStatus = processStatus;
    }

    public ProcessStatusDto getProcessStatus() {
        return processStatus;
    }
}
