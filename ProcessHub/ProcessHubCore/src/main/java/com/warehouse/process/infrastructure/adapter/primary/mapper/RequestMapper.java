package com.warehouse.process.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.enumeration.DeviceUserType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.process.domain.enumeration.ProcessStatus;
import com.warehouse.process.domain.model.DeviceInformation;
import com.warehouse.process.domain.model.InitializeProcessCommand;
import com.warehouse.process.infrastructure.dto.DeviceInformationDto;
import com.warehouse.process.infrastructure.dto.InitializeProcessRequestDto;
import com.warehouse.process.infrastructure.dto.ProcessLogId;
import com.warehouse.process.infrastructure.dto.ProcessStatusDto;

public abstract class RequestMapper {

    public static InitializeProcessCommand map(final InitializeProcessRequestDto request) {
        return InitializeProcessCommand.builder()
                .request(request.request())
                .deviceInformation(map(request.deviceInformation()))
                .build();
    }

    public static DeviceInformation map(final DeviceInformationDto deviceInformation) {
        final DeviceId deviceId = new DeviceId(deviceInformation.deviceId().value());
        final String version = deviceInformation.version();
        final DepartmentCode departmentCode = new DepartmentCode(deviceInformation.departmentCode().value());
        final UserId userId = new UserId(deviceInformation.userId().value());
        final String deviceType = deviceInformation.deviceType().name();
        final String deviceUserType = deviceInformation.deviceUserType().name();
        return new DeviceInformation(deviceId, departmentCode, userId, DeviceType.valueOf(deviceType),
                DeviceUserType.valueOf(deviceUserType), version);
    }

    public static ProcessStatus map(final ProcessStatusDto processStatus) {
        return ProcessStatus.valueOf(processStatus.name());
    }

    public static ProcessId map(final ProcessLogId processLogId) {
        return new ProcessId(processLogId.value());
    }
}
