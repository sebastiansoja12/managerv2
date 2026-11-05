package com.warehouse.organisationstructure.deliveryarea.domain.exception;

import com.warehouse.commonassets.exception.ProblemDetailsException;

import java.util.Map;

public abstract class DeliveryAreaException extends ProblemDetailsException {

    protected DeliveryAreaException(final int status, final String code, final String detail) {
        super("about:blank", "Delivery area request rejected", status, detail, null, Map.of("code", code));
    }
}
