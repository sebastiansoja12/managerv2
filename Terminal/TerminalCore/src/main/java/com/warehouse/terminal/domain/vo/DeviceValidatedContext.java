package com.warehouse.terminal.domain.vo;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.ProcessId;

import java.time.LocalDateTime;

public record DeviceValidatedContext(
        ProcessId processId,
        DeviceId deviceId,
        ProcessType processType,
        ServiceType sourceServiceType,
        ServiceType targetServiceType,
        LocalDateTime timestamp) {

    public static DeviceValidatedContext from(final DeviceValidationRequest request) {
        return new DeviceValidatedContext(
                request.processId(),
                request.deviceId(),
                request.processType(),
                request.sourceServiceType(),
                request.targetServiceType(),
                LocalDateTime.now());
    }
}
