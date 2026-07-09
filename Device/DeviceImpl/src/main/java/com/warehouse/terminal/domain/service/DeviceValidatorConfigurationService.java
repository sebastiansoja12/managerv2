package com.warehouse.terminal.domain.service;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.vo.DeviceValidation;
import org.springframework.stereotype.Service;

import com.warehouse.terminal.domain.port.secondary.SoftwareConfigurationServicePort;

@Service
public class DeviceValidatorConfigurationService {

    private final SoftwareConfigurationServicePort softwareConfigurationServicePort;

    public DeviceValidatorConfigurationService(final SoftwareConfigurationServicePort softwareConfigurationServicePort) {
        this.softwareConfigurationServicePort = softwareConfigurationServicePort;
    }

    public DeviceValidation validateSoftwareConfiguration(final DeviceId deviceId) {
        return softwareConfigurationServicePort.getSoftwareDeviceValidation(deviceId);
    }
}
