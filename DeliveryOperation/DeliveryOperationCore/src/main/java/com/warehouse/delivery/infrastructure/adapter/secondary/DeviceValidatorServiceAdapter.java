package com.warehouse.delivery.infrastructure.adapter.secondary;

import com.warehouse.delivery.domain.port.secondary.DeviceValidatorServicePort;
import com.warehouse.delivery.domain.vo.DeviceInformation;
import com.warehouse.terminal.DeviceEventPublisher;

import com.warehouse.terminal.dto.*;
import com.warehouse.terminal.event.DeviceValidationEvent;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
public class DeviceValidatorServiceAdapter implements DeviceValidatorServicePort {

    private final DeviceEventPublisher deviceEventPublisher;

    public DeviceValidatorServiceAdapter(final DeviceEventPublisher deviceEventPublisher) {
        this.deviceEventPublisher = deviceEventPublisher;
    }

    @Override
    public void validateDevice(final DeviceInformation deviceInformation) {
        final DeviceIdDto deviceId = new DeviceIdDto(deviceInformation.getDeviceId().getValue());
        final DepartmentCodeDto departmentCode = new DepartmentCodeDto(deviceInformation.getDepartmentCode().getValue());
        final UserIdDto userId = new UserIdDto(1L);
        final VersionDto version = new VersionDto(deviceInformation.getVersion());
        final DeviceUserTypeDto deviceUserType = DeviceUserTypeDto.valueOf(deviceInformation.getDeviceUserType().name());
        final DeviceTypeDto deviceType = DeviceTypeDto.valueOf(deviceInformation.getDeviceType().name());
        final DeviceValidationRequestDto deviceValidationRequest = new DeviceValidationRequestDto(
                deviceId, departmentCode, userId, version, deviceUserType, deviceType, true
        );
        deviceEventPublisher.send(new DeviceValidationEvent(deviceValidationRequest, Instant.now()));
    }
}
