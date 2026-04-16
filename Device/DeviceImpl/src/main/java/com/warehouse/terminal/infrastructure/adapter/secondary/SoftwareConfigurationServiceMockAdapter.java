package com.warehouse.terminal.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.port.secondary.SoftwareConfigurationServicePort;
import com.warehouse.terminal.domain.vo.DeviceValidation;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SoftwareConfigurationServiceMockAdapter implements SoftwareConfigurationServicePort {


    @Override
    public DeviceValidation getSoftwareDeviceValidation(final DeviceId deviceId) {
        return new DeviceValidation(deviceId, true);
    }
}
