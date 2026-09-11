package com.warehouse.terminal.domain.service;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.model.Device;

public interface DevicePairValidationService {
    void validateOperator(final Device device, final UserId userId);
}
