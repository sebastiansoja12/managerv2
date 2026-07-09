package com.warehouse.terminal.domain.port.secondary;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.vo.DeviceValidation;

public interface SoftwareConfigurationServicePort {
    DeviceValidation getSoftwareDeviceValidation(final DeviceId deviceId);
}
