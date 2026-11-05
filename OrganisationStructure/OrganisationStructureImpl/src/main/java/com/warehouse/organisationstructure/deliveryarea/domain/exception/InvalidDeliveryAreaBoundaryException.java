package com.warehouse.organisationstructure.deliveryarea.domain.exception;

public class InvalidDeliveryAreaBoundaryException extends DeliveryAreaException {

    public InvalidDeliveryAreaBoundaryException(final String detail) {
        super(400, "INVALID_BOUNDARY", detail);
    }
}
