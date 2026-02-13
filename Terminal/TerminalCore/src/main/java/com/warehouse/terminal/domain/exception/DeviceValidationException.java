package com.warehouse.terminal.domain.exception;

import com.warehouse.commonassets.exception.ProcessException;
import com.warehouse.commonassets.exception.ProcessFailureDetails;

public class DeviceValidationException extends ProcessException {

    public DeviceValidationException(final ProcessFailureDetails processFailureDetails) {
        super(processFailureDetails);
    }
}
