package com.warehouse.logistics.infrastructure.adapter.secondary;

import java.util.Locale;

import com.warehouse.auth.UserApiService;
import com.warehouse.auth.infrastructure.dto.UserDto;
import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.logistics.domain.port.secondary.ProcessHubServicePort;
import com.warehouse.process.ProcessHubApiService;
import com.warehouse.process.infrastructure.dto.*;
import com.warehouse.terminal.DeviceInformation;
import com.warehouse.terminal.DeviceUserType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProcessHubServiceAdapter implements ProcessHubServicePort {

    private final ProcessHubApiService processHubApiService;

    private final UserApiService userApiService;

    public ProcessHubServiceAdapter(final ProcessHubApiService processHubApiService,
                                    final UserApiService userApiService) {
        this.processHubApiService = processHubApiService;
        this.userApiService = userApiService;
    }

    @Override
    public ProcessId initialize(final DeviceInformation deviceInformation, final String request) {
        final DeviceInformationDto dto = new DeviceInformationDto(
                new DeviceIdDto(deviceInformation.getDeviceId().value()),
                new DepartmentCodeDto(deviceInformation.getDepartmentCode().value()),
                getUserIdByUsername(deviceInformation.getUsername()),
                resolveDeviceType(deviceInformation.getDeviceType()),
                resolveDeviceUserType(deviceInformation.getDeviceUserType()),
                deviceInformation.getVersion()
        );
        final ProcessIdDto processId = this.processHubApiService
                .initialize(new InitializeProcessRequestDto(dto, request));
        log.info("Initialized process in ProcessHub: {}", processId.value());
        return new ProcessId(processId.value());
    }

    private UserIdDto getUserIdByUsername(final String username) {
        final UserDto user = this.userApiService.findByUsername(username);
        return new UserIdDto(user.userId().value());
    }

    private DeviceTypeDto resolveDeviceType(final DeviceType deviceType) {
        try {
            return DeviceTypeDto.valueOf(deviceType.name().toUpperCase(Locale.ROOT));
        } catch (final IllegalArgumentException ex) {
            log.warn("Unsupported device type [{}], fallback to TERMINAL", deviceType);
            return DeviceTypeDto.TERMINAL;
        }
    }

    private DeviceUserTypeDto resolveDeviceUserType(final DeviceUserType deviceUserType) {
        if (DeviceUserType.SUPPLIER.equals(deviceUserType)) {
            return DeviceUserTypeDto.SUPPLIER;
        }
        return DeviceUserTypeDto.OPERATOR;
    }
}
