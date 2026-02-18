package com.warehouse.process.domain.model;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.ProcessId;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class ProcessDeviceValidatedCommand {
    private ProcessId processId;
    private DeviceId deviceId;
    private ProcessType processType;
    private ServiceType sourceServiceType;
    private ServiceType targetServiceType;
    private LocalDateTime timestamp;


    public DeviceId getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(final DeviceId deviceId) {
        this.deviceId = deviceId;
    }

    public ProcessId getProcessId() {
        return processId;
    }

    public void setProcessId(final ProcessId processId) {
        this.processId = processId;
    }

    public ServiceType getSourceServiceType() {
        return sourceServiceType;
    }

    public void setSourceServiceType(final ServiceType sourceServiceType) {
        this.sourceServiceType = sourceServiceType;
    }

    public ServiceType getTargetServiceType() {
        return targetServiceType;
    }

    public void setTargetServiceType(final ServiceType targetServiceType) {
        this.targetServiceType = targetServiceType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public ProcessType getProcessType() {
        return processType;
    }

    public void setProcessType(final ProcessType processType) {
        this.processType = processType;
    }
}
