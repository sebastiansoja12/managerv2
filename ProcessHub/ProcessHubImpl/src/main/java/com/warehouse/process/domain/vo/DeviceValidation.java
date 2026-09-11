package com.warehouse.process.domain.vo;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.process.domain.model.ProcessDeviceValidatedCommand;

public record DeviceValidation(DeviceId deviceId, ProcessType processType, ServiceType serviceType,
                               ServiceType sourceServiceType, ServiceType targetServiceType, String request,
                               String response, String faultDescription) {

    public static DeviceValidation of(final ProcessDeviceValidatedCommand command) {
        return new DeviceValidation(command.getDeviceId(), command.getProcessType(), command.getSourceServiceType(),
                command.getSourceServiceType(), command.getTargetServiceType(), command.getRequest(),
                command.getResponse(), command.getFaultDescription());
    }
}
