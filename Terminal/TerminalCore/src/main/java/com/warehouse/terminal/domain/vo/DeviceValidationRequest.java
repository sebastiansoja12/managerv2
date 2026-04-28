package com.warehouse.terminal.domain.vo;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.enumeration.DeviceUserType;
import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.Username;
import com.warehouse.terminal.dto.DeviceValidationRequestDto;
import com.warehouse.terminal.event.DeviceValidationEvent;

public record DeviceValidationRequest(
        ProcessId processId,
        ServiceType sourceServiceType,
        ServiceType targetServiceType,
        DeviceId deviceId,
        DepartmentCode departmentCode,
        Username username,
        String version,
        DeviceUserType deviceUserType,
        DeviceType deviceType,
        Boolean active) {

    public static DeviceValidationRequest from(final DeviceValidationEvent event) {
        final DeviceValidationRequestDto request = event.getDeviceValidationRequest();
        return new DeviceValidationRequest(
                event.getProcessId(),
                event.getSourceServiceType(),
                event.getTargetServiceType(),
                new DeviceId(request.deviceId().value()),
                new DepartmentCode(request.departmentCode().value()),
                new Username(request.username().value()),
                request.version().value(),
                DeviceUserType.valueOf(request.deviceUserType().name()),
                DeviceType.valueOf(request.deviceType().name()),
                request.active());
    }
}
