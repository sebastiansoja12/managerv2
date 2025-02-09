package com.warehouse.logistics.infrastructure.adapter.secondary;

import com.warehouse.logistics.domain.port.secondary.DeviceAgentServicePort;
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

    public DeviceAgentServiceAdapter(final DeviceEventPublisher deviceEventPublisher) {
        this.deviceEventPublisher = deviceEventPublisher;
    }

    @Override
    public void validateDevice(final DeviceInformation deviceInformation) {
        final DeviceIdDto deviceId = new DeviceIdDto(deviceInformation.getDeviceId().getValue());
        final DepartmentCodeDto departmentCode = new DepartmentCodeDto(deviceInformation.getDepartmentCode().getValue());
        final UsernameDto username = new UsernameDto(deviceInformation.getUsername());
        final VersionDto version = new VersionDto(deviceInformation.getVersion());
        final DeviceUserTypeDto deviceUserType = DeviceUserTypeDto.valueOf(deviceInformation.getDeviceUserType().name());
        final DeviceTypeDto deviceType = DeviceTypeDto.valueOf(deviceInformation.getDeviceType().name());
        final DeviceValidationRequestDto deviceValidationRequest = new DeviceValidationRequestDto(
                deviceId, departmentCode, username, version, deviceUserType, deviceType, true
        );
        deviceEventPublisher.send(new DeviceValidationEvent(deviceValidationRequest, Instant.now()));
    }

    @Override
    public void updateDevice(final DeviceInformation deviceInformation) {
        final DeviceIdDto deviceId = new DeviceIdDto(deviceInformation.getDeviceId().getValue());
        final VersionDto version = new VersionDto(deviceInformation.getVersion());
        final DeviceUpdateRequestDto deviceUpdateRequest = new DeviceUpdateRequestDto(deviceId, version);
        deviceEventPublisher.send(new DeviceUpdateEvent(deviceUpdateRequest, Instant.now()));
    }
}
