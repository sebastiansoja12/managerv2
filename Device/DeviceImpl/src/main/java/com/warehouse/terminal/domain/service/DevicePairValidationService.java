package com.warehouse.terminal.domain.service;

import com.warehouse.terminal.domain.model.Device;
import com.warehouse.terminal.domain.model.command.DevicePairRequest;

public interface DevicePairValidationService {
    Device validate(final DevicePairRequest request);
}
