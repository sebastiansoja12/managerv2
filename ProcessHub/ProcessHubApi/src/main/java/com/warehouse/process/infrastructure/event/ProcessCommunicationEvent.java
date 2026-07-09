package com.warehouse.process.infrastructure.event;

import java.time.LocalDateTime;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.commonassets.identificator.ProcessId;

public class ProcessCommunicationEvent extends ProcessChangedEvent implements ProcessLogEvent {

    private final ProcessType processType;
    private final ServiceType targetServiceType;
    private final String request;
    private final String response;
    private final String faultDescription;

    public ProcessCommunicationEvent(final ProcessId processLogId,
                                     final ServiceType serviceType,
                                     final ServiceType targetServiceType,
                                     final ProcessType processType,
                                     final LocalDateTime createdAt,
                                     final String request,
                                     final String response,
                                     final String faultDescription) {
        super(processLogId, serviceType, createdAt);
        this.targetServiceType = targetServiceType;
        this.processType = processType;
        this.request = request;
        this.response = response;
        this.faultDescription = faultDescription;
    }

    public ProcessType getProcessType() {
        return processType;
    }

    public ServiceType getTargetServiceType() {
        return targetServiceType;
    }

    public String getRequest() {
        return request;
    }

    public String getResponse() {
        return response;
    }

    public String getFaultDescription() {
        return faultDescription;
    }
}
