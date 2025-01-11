package com.warehouse.terminal.domain.vo;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.infrastructure.adapter.secondary.api.SoftwareConfigurationDto;

public record DeviceValidation(DeviceId deviceId, Boolean validation) {

    public static DeviceValidation from(final SoftwareConfigurationDto softwareConfiguration) {
		return new DeviceValidation(new DeviceId(Long.parseLong(softwareConfiguration.name())),
				Boolean.parseBoolean(softwareConfiguration.value()));
    }
}
