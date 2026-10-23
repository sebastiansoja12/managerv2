package com.warehouse.logistics.infrastructure.adapter.secondary;

import com.warehouse.logistics.application.port.secondary.DeviceAgentServicePort;
import com.warehouse.logistics.application.port.secondary.DeviceValidateCommand;
import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.process.ProcessHubEventPublisher;
import com.warehouse.process.infrastructure.event.ProcessDeviceValidatedEvent;
import com.warehouse.terminal.DeviceInformation;
import com.warehouse.terminal.DeviceEventPublisher;

import com.warehouse.terminal.dto.*;
import com.warehouse.terminal.event.DeviceUpdateEvent;
import com.warehouse.terminal.event.DeviceValidationEvent;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
public class DeviceAgentServiceAdapter implements DeviceAgentServicePort {

    private final DeviceEventPublisher deviceEventPublisher;
    
    private final ProcessHubEventPublisher processHubEventPublisher;

    public DeviceAgentServiceAdapter(final DeviceEventPublisher deviceEventPublisher, 
                                     final ProcessHubEventPublisher processHubEventPublisher) {
        this.deviceEventPublisher = deviceEventPublisher;
        this.processHubEventPublisher = processHubEventPublisher;
    }

    @Override
    public void validateDevice(final DeviceValidateCommand command) {
        final ProcessType processType = command.getProcessType();
        final DeviceInformation deviceInformation = command.getDeviceInformation();
        final DeviceIdDto deviceId = new DeviceIdDto(deviceInformation.getDeviceId().value());
        final DepartmentCodeDto departmentCode = new DepartmentCodeDto(deviceInformation.getDepartmentCode().getValue());
        final UsernameDto username = new UsernameDto(deviceInformation.getUsername());
        final VersionDto version = new VersionDto(deviceInformation.getVersion());
        final DeviceUserTypeDto deviceUserType = DeviceUserTypeDto.valueOf(deviceInformation.getDeviceUserType().name());
        final DeviceTypeDto deviceType = DeviceTypeDto.valueOf(deviceInformation.getDeviceType().name());
        final DeviceValidationRequestDto deviceValidationRequest = new DeviceValidationRequestDto(
                deviceId, departmentCode, username, version, deviceUserType, deviceType, true
        );
        final String requestLog = ("DeviceValidationRequest{deviceId=%s, username=%s, departmentCode=%s, version=%s, "
                + "deviceType=%s, deviceUserType=%s}").formatted(deviceId.value(), username.value(),
                departmentCode.value(), version.value(), deviceType, deviceUserType);
        try {
            deviceEventPublisher.send(new DeviceValidationEvent(deviceValidationRequest, Instant.now(),
                    command.getProcessId(), processType, ServiceType.LOGISTICS_ORCHESTRATOR));
        } catch (final RuntimeException exception) {
            publishDeviceValidated(command, requestLog, null, exception.getMessage());
            throw exception;
        }
        publishDeviceValidated(command, requestLog, "DeviceValidationResponse{valid=true}", null);
    }

    private void publishDeviceValidated(final DeviceValidateCommand command,
                                        final String requestLog,
                                        final String responseLog,
                                        final String faultDescription) {
        processHubEventPublisher.publish(new ProcessDeviceValidatedEvent(command.getProcessId(),
                command.getDeviceInformation().getDeviceId(), command.getProcessType(),
                ServiceType.LOGISTICS_ORCHESTRATOR, requestLog, responseLog, faultDescription));
    }

    @Override
    public void updateDevice(final DeviceInformation deviceInformation) {
        final DeviceIdDto deviceId = new DeviceIdDto(deviceInformation.getDeviceId().value());
        final VersionDto version = new VersionDto(deviceInformation.getVersion());
        final DeviceUpdateRequestDto deviceUpdateRequest = new DeviceUpdateRequestDto(deviceId, version);
        deviceEventPublisher.send(new DeviceUpdateEvent(deviceUpdateRequest, Instant.now()));
    }
}
