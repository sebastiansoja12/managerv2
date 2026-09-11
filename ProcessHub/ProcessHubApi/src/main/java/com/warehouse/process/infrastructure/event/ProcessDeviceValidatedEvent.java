package com.warehouse.process.infrastructure.event;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.ProcessId;

import java.time.LocalDateTime;

public class ProcessDeviceValidatedEvent implements ProcessLogEvent {

    private final ProcessId processId;
    private final DeviceId deviceId;
    private final ProcessType processType;
    private final ServiceType sourceServiceType;
    private final ServiceType targetServiceType = ServiceType.TERMINAL;
    private final LocalDateTime timestamp;
    private final String request;
    private final String response;
    private final String faultDescription;

    public ProcessDeviceValidatedEvent(final ProcessId processId, final DeviceId deviceId, final ProcessType processType,
                                       final ServiceType sourceServiceType, final String request,
                                       final String response, final String faultDescription) {
        this.processId = processId;
        this.deviceId = deviceId;
        this.processType = processType;
        this.sourceServiceType = sourceServiceType;
        this.timestamp = LocalDateTime.now();
        this.request = request;
        this.response = response;
        this.faultDescription = faultDescription;
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

    public ProcessId getProcessId() {
        return processId;
    }

    public DeviceId getDeviceId() {
        return deviceId;
    }

    public ServiceType getSourceServiceType() {
        return sourceServiceType;
    }

    public ServiceType getTargetServiceType() {
        return targetServiceType;
    }

    public ProcessType getProcessType() {
        return processType;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return timestamp;
    }
}
