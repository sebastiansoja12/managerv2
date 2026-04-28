package com.warehouse.terminal.event;

import java.time.Instant;

import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.terminal.dto.DeviceValidationRequestDto;

import lombok.Builder;

public class DeviceValidationEvent extends DeviceEvent implements AgentEvent {

    private final DeviceValidationRequestDto deviceValidationRequest;
    private final ProcessId processId;
    private final ServiceType sourceServiceType;
    private final ServiceType targetServiceType;

    @Builder
    public DeviceValidationEvent(
            final DeviceValidationRequestDto deviceValidationRequest, final Instant timestamp, final ProcessId processId,
            final ServiceType sourceServiceType) {
        super(timestamp);
        this.deviceValidationRequest = deviceValidationRequest;
        this.processId = processId;
        this.sourceServiceType = sourceServiceType;
        this.targetServiceType = ServiceType.TERMINAL;
    }

    public DeviceValidationRequestDto getDeviceValidationRequest() {
        return deviceValidationRequest;
    }

    public ProcessId getProcessId() {
        return processId;
    }

    public ServiceType getSourceServiceType() {
        return sourceServiceType;
    }

    public ServiceType getTargetServiceType() {
        return targetServiceType;
    }
}
